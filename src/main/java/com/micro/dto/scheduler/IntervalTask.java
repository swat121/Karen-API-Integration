package com.micro.dto.scheduler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntervalTask {
    private String taskName;
    private Long updateMillisTime;
    private String clientName;
    private String module;
    private String moduleId;
}
