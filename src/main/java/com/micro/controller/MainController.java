package com.micro.controller;



import com.micro.model.GarryResponse;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.rmi.ConnectIOException;


@RestController
@RequiredArgsConstructor
public class MainController {

    private final String resourceUrlGarry;
    private final RestTemplate restTemplate;
    @SneakyThrows
    @GetMapping("/garry/{key}")
    public String data(@PathVariable(value="key") String key){
        try {
        ResponseEntity<GarryResponse> response;
            switch (key) {
                case "temperature":
                    response = restTemplate.getForEntity(resourceUrlGarry + key, GarryResponse.class);
                    return response.getBody().getTemperature();
                case "backlightOn":
                    response = restTemplate.getForEntity(resourceUrlGarry + "setting?backlight=on", GarryResponse.class);
                    return response.getBody().getBacklight();
                case "backlightOff":
                    response = restTemplate.getForEntity(resourceUrlGarry + "setting?backlight=off", GarryResponse.class);
                    return response.getBody().getBacklight();
                default:
                    return "Такое еще не придумал";
            }
        } catch (Exception e){
            return "Проверьте подключение ESP к сети";
        }
    }
    @GetMapping("/garry/message/{text}")
    public String message(@PathVariable(value = "text") String text){
        ResponseEntity<GarryResponse> response
                = restTemplate.getForEntity(resourceUrlGarry+"message?text="+text, GarryResponse.class);
        return response.getBody().getMessage();
    }
}

