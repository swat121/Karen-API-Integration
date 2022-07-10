package com.micro.service;

import com.micro.config.UrlConfig;
import com.micro.model.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {
    //private final HashMap<String, String> resourceUrl;
    private final UrlConfig urlConfig;
    private final RestTemplate restTemplate;
    private ResponseEntity<DataResponse> response;

    public String request(String name, String key){
        try {
            switch (key) {
                case "help":
                    response = restTemplate.getForEntity(urlConfig.getResourceUrl().get(name) + key, DataResponse.class);
                    return response.getBody().getName() +": "+ response.getBody().getHelp();
                case "status":
                    ResponseEntity<String> status
                         = restTemplate.getForEntity(urlConfig.getResourceUrl().get(name) + key, String.class);
                    return status.getBody();
                default:
                    return "Такое еще не придумал";
            }
        } catch (Exception e){
            return "Проверьте подключение ESP к сети: " + e.getMessage();
        }
    }
    public String message(String name, String key, String text){
        response = restTemplate.getForEntity(urlConfig.getResourceUrl().get(name) + key + "?text="+text, DataResponse.class);
        return response.getBody().getMessage();
    }
    public String setting(String name, String key) {
        try {
            response = restTemplate.getForEntity(urlConfig.getResourceUrl().get(name)+key, DataResponse.class);
        }catch (Exception e){
            return "Проверьте подключение ESP к сети: " + e.getMessage();
        }
        switch (key){
            case "relay1":
                return response.getBody().getRelay1();
            case "relay2":
                return response.getBody().getRelay2();
            case "relay3":
                return response.getBody().getRelay3();
            case "backlight":
                return response.getBody().getBacklight();
            default:
                return "Такое еще не придумал";
        }
    }
    public String sensor(String name, String key) {
        try {
            response = restTemplate.getForEntity(urlConfig.getResourceUrl().get(name)+key, DataResponse.class);
        }catch (Exception e){
            return "Проверьте подключение ESP к сети: " + e.getMessage();
        }
        switch (key){
            case "temperature":
                return response.getBody().getTemperature();
            case "light":
                return response.getBody().getLight();
            default:
                return "Такое еще не придумал";
        }
    }
}
