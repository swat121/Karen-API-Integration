package com.micro.service;

import com.micro.endpoints.TempEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class TempService {
    private final MicroControllerService microControllerService;
    private final ConnectionService connectionService;
    private final long updateTime = 3600000;

    private int getDegrees() {
        try {
            return Integer.parseInt(microControllerService.sensor("patric", "temperature"));
        } catch (ResourceAccessException | IllegalStateException e) {
            return -1000;
        }
    }

    @Scheduled(fixedRate = updateTime)
    private void setDegrees() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Integer> map= new LinkedMultiValueMap<>();
        map.add("degrees", getDegrees());
        HttpEntity<MultiValueMap<String, Integer>> request = new HttpEntity<>(map, headers);
        connectionService.postRequestForService("karen-data", TempEndpoints.API_TEMP, request);
    }
}
