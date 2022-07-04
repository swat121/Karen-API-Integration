package com.micro.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class GarryResponse {
    @JsonProperty("temp")
    private String temperature;

    private String backlight;

    private String message;
}
