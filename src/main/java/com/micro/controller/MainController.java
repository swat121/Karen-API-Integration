package com.micro.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.model.Data;
import com.micro.util.NodeMCU_Garry;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class MainController {

    private NodeMCU_Garry nodeMCUGarry;
    private ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @GetMapping("/garry/temperature")
    public String temperature(){
        String json = nodeMCUGarry.getTemperature();
//        Data data = objectMapper.readValue(json,Data.class);
        JsonNode jsonNode = objectMapper.readTree(json);
        String temp = jsonNode.get("temp").asText();
        return temp;
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
