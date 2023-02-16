package com.micro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ConnectionService {
    private final RestTemplate restTemplate;

    public <T> T getResponseFromService(String name, String url, Class<T> responseType) {
        return restTemplate.getForEntity("http://" + name + url, responseType).getBody();
    }

    public String postRequestForService(String name, String url, HttpEntity<MultiValueMap<String, Integer>> request) {
        return restTemplate.postForEntity("http://" + name + url, request, String.class).getBody();
    }
}
