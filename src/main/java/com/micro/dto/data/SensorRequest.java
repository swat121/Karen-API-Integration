package com.micro.dto.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorRequest {
    private String name;
    private String sensorId;
    private String data;
}
