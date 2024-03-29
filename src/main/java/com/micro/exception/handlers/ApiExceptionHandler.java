package com.micro.exception.handlers;

import com.micro.exception.ApiRequestException;
import com.micro.exception.ErrorCode;
import com.micro.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(buildErrorResponse("ApiRequestException: " + e.getMessage()), errorCode.getHttpStatus());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleBadRequestException(IllegalArgumentException ex) {
        return buildErrorResponse("IllegalArgumentException: " + ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        return buildErrorResponse("Exception: " + e.getMessage());
    }

    private ErrorResponse buildErrorResponse(String message) {
        return new ErrorResponse(
                message,
                ZonedDateTime.now()
        );
    }
}
