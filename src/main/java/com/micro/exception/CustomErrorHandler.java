package com.micro.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (
                httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR ||
                        httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR
        );
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        String responseBody = new String(httpResponse.getBody().readAllBytes());

        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);

        switch (httpResponse.getStatusCode().series()) {
            case CLIENT_ERROR -> {
                if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new ApiRequestException(ErrorCode.ENTITY_NOT_FOUND, errorResponse.getMessage());
                }
                if (httpResponse.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                    throw new ApiRequestException(ErrorCode.UNPROCESSABLE_ENTITY, errorResponse.getMessage());
                }
            }
            case SERVER_ERROR -> throw new ApiRequestException(ErrorCode.SERVER_ERROR, errorResponse.getMessage());
        }
    }
}

