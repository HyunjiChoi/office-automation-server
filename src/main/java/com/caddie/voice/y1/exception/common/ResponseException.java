package com.caddie.voice.y1.exception.common;

import lombok.Data;

@Data
public class ResponseException extends RuntimeException {
    public ExceptionType exceptionType;
    public ResponseException(ExceptionType exceptionType){
        this.exceptionType = exceptionType;
    }
}
