package com.micro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ApiRequestException.class)
    public ErrorResponse handleApiRequestException(ApiRequestException e) {
        return buildErrorResponse("ApiRequestException: " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ResourceAccessException.class)
    public ErrorResponse handleConnectException(ResourceAccessException e) {
        return buildErrorResponse("ResourceAccessException: " + e.getMessage());
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
