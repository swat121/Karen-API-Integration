package com.micro.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
public class ErrorResponse {
    private final String message;
    private final ZonedDateTime time;

    @JsonCreator
    public ErrorResponse(@JsonProperty("message") String message,
                         @JsonProperty("time") ZonedDateTime time) {
        this.message = message;
        this.time = time;
    }
}
