package com.caddie.voice.y1.controller;


import com.caddie.voice.y1.dto.LoginRequest;
import com.caddie.voice.y1.dto.RefreshTokenRequest;
import com.caddie.voice.y1.handler.ApiResponse;
import com.caddie.voice.y1.service.LoginService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/vc-settlement/v1")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ApiResponse main(@RequestBody LoginRequest loginRequest){
        return loginService.login(loginRequest.getId(),loginRequest.getPw());
    }

    @ApiOperation("토큰갱신")
    @PostMapping("/refresh-token")
    public ApiResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return loginService.refreshToken(refreshTokenRequest.getRefreshToken());
    }

}
