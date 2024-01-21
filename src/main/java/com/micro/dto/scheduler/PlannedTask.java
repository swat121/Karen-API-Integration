package com.micro.dto.scheduler;

import lombok.Data;

@Data
public class PlannedTask {
    private String taskName;
    private int hours;
    private int minute;
    private String clientName;
    private String module;
    private String moduleId;
}
