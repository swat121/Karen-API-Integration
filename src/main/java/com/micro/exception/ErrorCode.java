package com.micro.exception;

public enum ErrorCode {
    ENTITY_NOT_FOUND(404, "Entity - Not Found"),
    UNPROCESSABLE_ENTITY(422, "Wrong entity request"),
    SERVER_ERROR(500, "Server Error");

    private final int httpStatus;
    private final String description;

    ErrorCode(int httpStatus, String description) {
        this.httpStatus = httpStatus;
        this.description = description;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getDescription() {
        return description;
    }
}

