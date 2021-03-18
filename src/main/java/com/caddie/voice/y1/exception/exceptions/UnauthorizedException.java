package com.caddie.voice.y1.exception.exceptions;

import com.caddie.voice.y1.exception.common.ExceptionType;
import com.caddie.voice.y1.exception.common.ResponseException;

public class UnauthorizedException extends ResponseException {
    public UnauthorizedException(ExceptionType unauthorizedException) {
        super(unauthorizedException);
    }
}
