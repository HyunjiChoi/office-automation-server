package com.caddie.voice.y1.exception.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ExceptionType {
    UnauthorizedException("UnauthorizedException",HttpStatus.UNAUTHORIZED.value(),  HttpStatus.UNAUTHORIZED),
    NotExistIdException("NOT EXIST ID",HttpStatus.UNAUTHORIZED.value(),  HttpStatus.SWITCHING_PROTOCOLS),
    AuthExpireUserException("Token expiration",110,HttpStatus.OK),
    NotMatchPwException("NOT MATCH PW",HttpStatus.UNAUTHORIZED.value(),  HttpStatus.SWITCHING_PROTOCOLS),
    ExistSalesException("EXIST SALES FILE",406,  HttpStatus.NOT_ACCEPTABLE),
    ExistShippingsException("EXIST SHIPPINGS FILE",406,  HttpStatus.NOT_ACCEPTABLE);

    @Getter
    private String message;
    @Getter
    private int code;
    @Getter
    private HttpStatus httpStatus;



    ExceptionType(String message, int code, HttpStatus httpStatus) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
