package com.micro.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class GarryResponse {
    private String name;
    @JsonProperty("temp")
    private String temperature;

    private String light;

    private String backlight;

    private String message;

    private String relay1;
    private String relay2;
    private String relay3;

    private String help;
}
