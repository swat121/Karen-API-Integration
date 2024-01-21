package com.micro.exception.handlers;

import com.micro.exception.ErrorResponse;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Order(PriorityOrdered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ResourceAccessExceptionHandler {

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ResourceAccessException.class)
    public ErrorResponse handleConnectException(ResourceAccessException e) {
        return buildErrorResponse("ResourceAccessException: " + processErrorMessage(e.getMessage()));
    }

    private String processErrorMessage(String originalMessage) {
        String requestPattern = "([A-Z]+) request for \"([^\"]+)\"";
        Pattern pattern = Pattern.compile(requestPattern);
        Matcher matcher = pattern.matcher(originalMessage);

        String requestType = "";
        String url = "";
        if (matcher.find()) {
            requestType = matcher.group(1);
            url = matcher.group(2);
        }

        return "Unable to perform " + requestType + " request to " + url + ". The service might be down or there could be connection issues.";
    }

    private ErrorResponse buildErrorResponse(String message) {
        return new ErrorResponse(
                message,
                ZonedDateTime.now()
        );
    }
}
