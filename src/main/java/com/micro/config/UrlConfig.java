package com.micro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class UrlConfig {
    @Bean
    public HashMap<String, String> resourceUrl(){
        HashMap<String, String> name = new HashMap<>();
        name.put("garry", "http://192.168.0.100:80/");
        name.put("patric","");
        name.put("spongebob","");
        return name;
    };
}
