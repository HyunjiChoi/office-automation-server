package com.caddie.voice.y1.exception.common;

import com.caddie.voice.y1.exception.exceptions.*;
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

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<ErrorResponse> handleUnauthorizedException(final UnauthorizedException e){
        ErrorResponse errorResponse = new ErrorResponse(401,ExceptionType.AuthExpireUserException);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotExistIdException.class)
    protected ResponseEntity<ErrorResponse> handleNotExistIdException(final NotExistIdException e){
        ErrorResponse errorResponse = new ErrorResponse(101,ExceptionType.NotExistIdException);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(NotMatchPwException.class)
    protected ResponseEntity<ErrorResponse> handleNotMatchPwException(final NotMatchPwException e){
        ErrorResponse errorResponse = new ErrorResponse(101,ExceptionType.NotMatchPwException);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(ExistSalesException.class)
    protected ResponseEntity<ErrorResponse> handleExistSalesException(final ExistSalesException e){
        ErrorResponse errorResponse = new ErrorResponse(406,ExceptionType.ExistSalesException);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @ExceptionHandler(ExistShippingsException.class)
    protected ResponseEntity<ErrorResponse> handleExistShippingsException(final ExistShippingsException e){
        ErrorResponse errorResponse = new ErrorResponse(406,ExceptionType.ExistShippingsException);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
}
