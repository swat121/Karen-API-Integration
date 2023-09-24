package com.micro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class BoardService {

    @Value("${board.port}")
    private String port;

    private final ConnectionService connectionService;
    private final ClientService clientService;

    private String makeRequest(String name, String module, String id, String endpoint, HttpMethod method, @Nullable HttpEntity<MultiValueMap<String, String>> requestEntity) {
        String baseUrl = String.format("http://%s:%s/api/v1/%s", clientService.getClient(name).getIp(), port, endpoint);

        String requestUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/{module}/{id}")
                .buildAndExpand(module, id)
                .toUriString();

        return connectionService.requestForBoard(requestUrl, method, requestEntity, String.class);
    }

    public String makeSensorRequest(String name, String module, String id) {
        return makeRequest(name, module, id, "sensors", HttpMethod.GET, null);
    }

    public String makeTrackerRequest(String name, String module, String id) {
        return makeRequest(name, module, id, "trackers", HttpMethod.GET, null);
    }

    public String makeSwitcherRequest(String name, String module, String id) {
        return makeRequest(name, module, id, "switchers", HttpMethod.PUT, null);
    }
}
