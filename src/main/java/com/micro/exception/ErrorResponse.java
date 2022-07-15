package com.micro.exception;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ErrorResponse {
    private final String message;
    private final ZonedDateTime time;
}
