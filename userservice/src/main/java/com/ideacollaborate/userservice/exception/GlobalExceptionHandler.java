package com.ideacollaborate.userservice.exception;

import com.ideacollaborate.userservice.exception.auth.InvalidCredentialsException;
import com.ideacollaborate.userservice.exception.auth.InvalidTokenException;
import com.ideacollaborate.userservice.exception.user.UserAlreadyExistsException;
import com.ideacollaborate.userservice.exception.user.UserNotFoundException;
import com.ideacollaborate.userservice.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getErrorCode().code(), ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getErrorCode().code(), ex.getMessage()));
    }

    @ExceptionHandler({
            InvalidTokenException.class,
            InvalidCredentialsException.class
    })
    public ResponseEntity<String> handleAuthExceptions(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ErrorCode.INTERNAL_ERROR.code(), ex.getMessage()));
    }
}
