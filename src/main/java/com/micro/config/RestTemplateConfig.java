package com.micro.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.exception.CustomErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@Configuration
public class RestTemplateConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(5))
                .errorHandler(new CustomErrorHandler(objectMapper))
                .interceptors(new LoggingRequestInterceptor())
                .build();
    }
}
