package com.micro.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataResponse {
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
