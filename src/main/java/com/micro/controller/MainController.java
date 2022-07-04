package com.micro.controller;



import com.micro.model.GarryResponse;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequiredArgsConstructor
public class MainController {

    private final RestTemplate restTemplate;
    @SneakyThrows
    @GetMapping("/garry/temperature")
    public String temperature(){
        String fooResourceUrl
                = "http://192.168.0.100:80/temperature";
        ResponseEntity<GarryResponse> response
                = restTemplate.getForEntity(fooResourceUrl, GarryResponse.class);
        return response.getBody().getTemperature();
    }
    @GetMapping("/garry/backlightOn")
    public String backlightOn(){
        String fooResourceUrl
                = "http://192.168.0.100:80//setting?backlight=on";
        ResponseEntity<GarryResponse> response
                = restTemplate.getForEntity(fooResourceUrl, GarryResponse.class);
        return response.getBody().getBacklight();
    }
    @GetMapping("/garry/backlightOff")
    public String backlightOff(){
        String fooResourceUrl
                = "http://192.168.0.100:80//setting?backlight=off";
        ResponseEntity<GarryResponse> response
                = restTemplate.getForEntity(fooResourceUrl, GarryResponse.class);
        return response.getBody().getBacklight();
    }
    @GetMapping("/garry/message/{text}")
    public String message(@PathVariable(value = "text") String text){
        String fooResourceUrl
                = "http://192.168.0.100:80//message?text=";
        ResponseEntity<GarryResponse> response
                = restTemplate.getForEntity(fooResourceUrl+text, GarryResponse.class);
        return response.getBody().getMessage();
    }
}

