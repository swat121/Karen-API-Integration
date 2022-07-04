package com.micro.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.model.GarryResponse;
import com.micro.util.NodeMCU_Garry;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequiredArgsConstructor
public class MainController {

    private final NodeMCU_Garry nodeMCUGarry;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    @SneakyThrows
    @GetMapping("/garry/temperature")
    public String temperature(){
        String fooResourceUrl
                = "http://192.168.0.100:80/temperature";
        ResponseEntity<GarryResponse> response
                = restTemplate.getForEntity(fooResourceUrl, GarryResponse.class);
//        String json = nodeMCUGarry.getTemperature();
////        Data data = objectMapper.readValue(json,Data.class);
//        JsonNode jsonNode = objectMapper.readTree(json);
//        String temp = jsonNode.get("temp").asText();
        return response.getBody().getTemperature();
    }
    @GetMapping("/garry/backlightOn")
    public String backlightOn(){
        return nodeMCUGarry.setting("backlight","on");
    }
    @GetMapping("/garry/backlightOff")
    public String backlightOff(){
        return nodeMCUGarry.setting("backlight","off");
    }
}

