package com.micro.service;

import com.micro.model.GarryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {
    private final HashMap<String, String> resourceUrl;
    private final RestTemplate restTemplate;
    private ResponseEntity<GarryResponse> response;
    private boolean isBacklight = false;
    private boolean isRelay = false;

    public String request(String name, String key, String text){
        try {
            switch (key) {
                case "temperature":
                    response = restTemplate.getForEntity(resourceUrl.get(name) + key, GarryResponse.class);
                    return response.getBody().getTemperature();
                case "backlight":
                    isBacklight = !isBacklight;
                    response = restTemplate.getForEntity(resourceUrl.get(name) + "setting?"+key+"="+isBacklight, GarryResponse.class);
                    return response.getBody().getBacklight();
                case "relay":
                    isRelay = !isRelay;
                    response = restTemplate.getForEntity(resourceUrl.get(name) + "setting?"+key+"="+isRelay, GarryResponse.class);
                    return response.getBody().getRelay();
                case "message":
                    response = restTemplate.getForEntity(resourceUrl.get(name) + key + "?text="+text,GarryResponse.class);
                    return response.getBody().getMessage();
                case "help":
                    response = restTemplate.getForEntity(resourceUrl.get(name) + key, GarryResponse.class);
                    return response.getBody().getName() +": "+ response.getBody().getHelp();
                default:
                    return "Такое еще не придумал";
            }
        } catch (Exception e){
            return "Проверьте подключение ESP к сети: " + e.getMessage();
        }
    }
}
