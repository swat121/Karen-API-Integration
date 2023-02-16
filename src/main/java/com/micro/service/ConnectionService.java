package com.micro.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ConnectionService {
    private final RestTemplate restTemplate;
    private static final Logger LOG = LogManager.getRootLogger();

    public <T> T getResponseFromService(String name, String url, Class<T> responseType) {
        LOG.info("======================== Connection service: GET " + name + url + responseType + " ========================");
        return restTemplate.getForEntity("http://" + name + url, responseType).getBody();
    }

    public String postRequestForService(String name, String url, HttpEntity<MultiValueMap<String, Integer>> request) {
        LOG.info("======================== Connection service: POST " + name + url + request + " ========================");
        return restTemplate.postForEntity("http://" + name + url, request, String.class).getBody();
    }
}
