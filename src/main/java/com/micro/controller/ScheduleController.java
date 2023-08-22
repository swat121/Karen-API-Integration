package com.micro.controller;

import com.micro.service.DynamicSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final DynamicSchedulerService dynamicSchedulerService;

    @PostMapping("/api/v1/schedule/start")
    public ResponseEntity<String> startTask(@RequestParam String taskName, @RequestParam long updateTime) {
        dynamicSchedulerService.startTask(taskName, updateTime).join();
        return ResponseEntity.ok("Task: " + taskName + " started, wist update time is: " + updateTime);
    }

    @PostMapping("/api/v1/schedule/stop")
    public ResponseEntity<String> stopTask(@RequestParam String taskName) {
        dynamicSchedulerService.stopTask(taskName);
        return ResponseEntity.ok("Task: " + taskName + " stopped");
    }

    @PostMapping("/api/v1/schedule/planned")
    public ResponseEntity<String> start() {
        dynamicSchedulerService.scheduleOneTimeTask();
        return ResponseEntity.ok("OK");
    }
}
