package com.micro.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class ConnectionService {
    private final RestTemplate restTemplate;
    private final LoadBalancerClient loadBalancerClient;

    public <T> T requestForBoard(String url, HttpMethod method, @Nullable HttpEntity<MultiValueMap<String, String>> requestEntity, Class<T> responseType) {
        return restTemplate.exchange(url, method, requestEntity, responseType).getBody();
    }

    @SneakyThrows
    public <T> T getResponseFromService(String port, String url, Class<T> responseType) {
        URI backendUrl = URI.create("http://localhost:" + port).resolve(url);
        return restTemplate.getForObject(backendUrl, responseType);
    }

    @SneakyThrows
    public <T> String postRequestForService(String port, String url, HttpEntity<T> request) {
        URI backendUrl = URI.create("http://localhost:" + port).resolve(url);
        return restTemplate.postForObject(backendUrl, request, String.class);
    }

    @SneakyThrows
    public <T> void putRequestForService(String port, String url, HttpEntity<T> request) {
        URI backendUrl = URI.create("http://localhost:" + port).resolve(url);
        restTemplate.put(String.valueOf(backendUrl), request, String.class);
    }
}
