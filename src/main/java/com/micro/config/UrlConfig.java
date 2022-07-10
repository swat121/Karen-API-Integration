package com.micro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "service")
public class UrlConfig {
    private Map<String,String> resource;
}
