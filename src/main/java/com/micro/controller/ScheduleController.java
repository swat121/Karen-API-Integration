package com.micro.controller;

import com.micro.dto.scheduler.IntervalTask;
import com.micro.dto.scheduler.PlannedTask;
import com.micro.service.DynamicSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final DynamicSchedulerService dynamicSchedulerService;

    @PostMapping("/api/v1/schedule/interval/start")
    public ResponseEntity<String> startTask(@RequestBody IntervalTask body) {
        dynamicSchedulerService.startIntervalTask(body).join();

        String responseMessage = String.format(
                "Task '%s' started with an update interval of %dms for client '%s' (%s: %s).",
                body.getTaskName(),
                body.getUpdateMillisTime(),
                body.getClientName(),
                body.getModule(),
                body.getModuleId()
        );

        return new ResponseEntity<>(responseMessage, HttpStatus.ACCEPTED);
    }

    @PostMapping("/api/v1/schedule/stop")
    public ResponseEntity<String> stopTask(@RequestParam String taskName) {
        dynamicSchedulerService.stopTask(taskName);
        return new ResponseEntity<>("Task: " + taskName + " stopped", HttpStatus.ACCEPTED);
    }

    @PostMapping("/api/v1/schedule/planned")
    public ResponseEntity<String> plannedTask(@RequestBody PlannedTask body) {
        dynamicSchedulerService.startPlannedTask(body);

        String responseMessage = String.format(
                "Planned task '%s' set for %02d:%02d for client '%s' (%s: %s).",
                body.getTaskName(),
                body.getHours(),
                body.getMinute(),
                body.getClientName(),
                body.getModule(),
                body.getModuleId()
        );

        return new ResponseEntity<>(responseMessage, HttpStatus.ACCEPTED);
    }
}
