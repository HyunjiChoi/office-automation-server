package com.caddie.voice.y1.service;

import com.caddie.voice.y1.exception.common.ExceptionType;
import com.caddie.voice.y1.exception.exceptions.AuthException;
import com.caddie.voice.y1.exception.exceptions.NotExistIdException;
import com.caddie.voice.y1.exception.exceptions.NotMatchPwException;
import com.caddie.voice.y1.exception.exceptions.UnauthorizedException;
import com.caddie.voice.y1.handler.ApiResponse;
import com.caddie.voice.y1.handler.ApiResponseMap;
import com.caddie.voice.y1.handler.JwtTokenProvider;
import com.caddie.voice.y1.util.LoginInfo;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final LoginInfo loginInfo;

    public LoginService(JwtTokenProvider jwtTokenProvider, LoginInfo loginInfo) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginInfo = loginInfo;
    }

    public ApiResponse login(String id, String pw){

        //id 안맞음
        if(!id.equals(loginInfo.id)){
            throw new NotExistIdException(ExceptionType.NotExistIdException);
        }

        //비밀번호 안맞음
        if(!pw.equals(loginInfo.pw)){
            throw new NotMatchPwException(ExceptionType.NotMatchPwException);
        }

        String accessToken = jwtTokenProvider.createToken(id);
        String refreshToken = jwtTokenProvider.createRefreshToken(id);

        ApiResponseMap responseMap = new ApiResponseMap();
        responseMap.setResponseData("accessToken", accessToken);
        responseMap.setResponseData("refreshToken", refreshToken);

        return responseMap;
    }

    public ApiResponse refreshToken(String refreshToken){

        try {
            String id = jwtTokenProvider.getUserId(refreshToken);
            if (id != null) {
                    ApiResponseMap responseMap = new ApiResponseMap();
                    String updateAccessToken = jwtTokenProvider.createToken(id);
                    String updateRefreshToken = jwtTokenProvider.createRefreshToken(id);

                    responseMap.setResponseData("accessToken", updateAccessToken);
                    responseMap.setResponseData("refreshToken", updateRefreshToken);

                    return responseMap;
            }
            throw new AuthException(ExceptionType.AuthExpireUserException);
        }catch(Exception e){
            throw new AuthException(ExceptionType.AuthExpireUserException);
        }


    }
}
