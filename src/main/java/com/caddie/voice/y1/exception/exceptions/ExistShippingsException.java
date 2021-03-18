package com.caddie.voice.y1.exception.exceptions;

import com.caddie.voice.y1.exception.common.ExceptionType;
import com.caddie.voice.y1.exception.common.ResponseException;

public class ExistShippingsException extends ResponseException {
    public ExistShippingsException(ExceptionType existShippingsException) {
        super(existShippingsException);
    }
}
