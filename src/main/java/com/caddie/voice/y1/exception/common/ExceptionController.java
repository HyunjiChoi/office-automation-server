package com.caddie.voice.y1.exception.common;

import com.caddie.voice.y1.exception.exceptions.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(AuthException.class)
    protected ResponseEntity<ErrorResponse> handleAuthException(final AuthException e){
        return new ResponseEntity<>(new ErrorResponse(e.getExceptionType()), HttpStatus.OK);
    }
}
