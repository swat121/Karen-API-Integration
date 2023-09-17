package com.micro.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = LogManager.getRootLogger();

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        StringBuilder requestLog = new StringBuilder();
        StringBuilder responseLog = new StringBuilder();

        requestLog.append("\n---- [OUTGOING] REQUEST ---\n")
                .append("[OUTGOING] URI: ").append(request.getURI()).append("\n")
                .append("[OUTGOING] Method: ").append(request.getMethod()).append("\n")
                .append("[OUTGOING] Headers: ").append(request.getHeaders()).append("\n")
                .append("[OUTGOING] Body: ").append(new String(body, StandardCharsets.UTF_8)).append("\n");

        LOG.info(requestLog.toString());

        ClientHttpResponse response = execution.execute(request, body);

        byte[] responseBody = StreamUtils.copyToByteArray(response.getBody());

        responseLog.append("\n---- [OUTGOING] RESPONSE ---- for request ").append(request.getMethod()).append(": ").append(request.getURI()).append("\n")
                .append("[OUTGOING] Status: ").append(response.getStatusCode()).append("\n")
                .append("[OUTGOING] Headers: ").append(response.getHeaders()).append("\n")
                .append("[OUTGOING] Body: ").append(new String(responseBody, StandardCharsets.UTF_8)).append("\n");

        LOG.info(responseLog.toString());

        return new ClientHttpResponseWrapper(response, responseBody);
    }

    private static class ClientHttpResponseWrapper implements ClientHttpResponse {
        private final ClientHttpResponse response;
        private final byte[] body;

        public ClientHttpResponseWrapper(ClientHttpResponse response, byte[] body) {
            this.response = response;
            this.body = body;
        }

        @Override
        public InputStream getBody() {
            return new ByteArrayInputStream(body);
        }

        // Далее делегируем все остальные методы к исходному response

        @Override
        public HttpHeaders getHeaders() {
            return response.getHeaders();
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return response.getStatusCode();
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return response.getRawStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return response.getStatusText();
        }

        @Override
        public void close() {
            response.close();
        }
    }
}
