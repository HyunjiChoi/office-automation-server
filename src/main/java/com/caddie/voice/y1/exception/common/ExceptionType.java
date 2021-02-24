package com.caddie.voice.y1.exception.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ExceptionType {
    UnauthorizedException("UnauthorizedException",HttpStatus.UNAUTHORIZED.value(),  HttpStatus.UNAUTHORIZED);

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
