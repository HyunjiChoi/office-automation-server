package com.caddie.voice.y1.exception.common;

import lombok.Data;
import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private HttpStatus httpStatus;
    private int code;
    private Object error;

    public ErrorResponse(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.code = httpStatus.value();
        this.error = HttpStatus.UNAUTHORIZED.toString();
    }


    public ErrorResponse(int code, Object message) {
        this.code = code;
        this.error =  message;
    }

    public ErrorResponse(ExceptionType exceptionType){
        this.code = exceptionType.getCode();
        this.error =  new ErrorMessage(exceptionType.getMessage());
    }

    public ErrorResponse(ExceptionType exceptionType, Object errorData){
        this.code = exceptionType.getCode();
        this.error = errorData;
    }

    public int getCode(){
        return this.code;
    }

    public Object getError(){
        return this.error;
    }



    @Data
    public static class ErrorDetails {
        private String fieldName;
        private String message;
    }
}
