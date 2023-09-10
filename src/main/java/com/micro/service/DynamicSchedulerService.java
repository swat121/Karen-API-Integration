package com.micro.service;

import com.micro.dto.scheduler.IntervalTask;
import com.micro.dto.scheduler.PlannedTask;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;

@Service
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class DynamicSchedulerService {

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();
    private final BoardService boardService;

    @Async
    public synchronized CompletableFuture<Void> startIntervalTask(IntervalTask body) {
        return CompletableFuture.runAsync(() -> {
            if(scheduledTasks.get(body.getTaskName()) != null) {
                stopTask(body.getTaskName());
            }
            ScheduledFuture<?> futureTask = null;

            futureTask = threadPoolTaskScheduler.scheduleWithFixedDelay(() -> executeSensorIntervalTask(body), body.getUpdateMillisTime());

            scheduledTasks.put(body.getTaskName(), futureTask);
        });
    }

    public synchronized void stopTask(String taskName) {
        ScheduledFuture<?> existingTask = scheduledTasks.get(taskName);
        if (existingTask != null) {
            existingTask.cancel(true);
            scheduledTasks.remove(taskName);
        } else {
            throw new IllegalArgumentException("Unknown task name: " + taskName);
        }
    }

    public void startPlannedTask(PlannedTask body) {
        Date plannedDate = getPlannedDate(body.getHours(), body.getMinute());
        ScheduledFuture<?> futureTask = threadPoolTaskScheduler.schedule(
                () -> executeSwitcherPlannedTask(body),
                plannedDate
        );
        scheduledTasks.put(body.getTaskName(), futureTask);
    }

    private Date getPlannedDate(int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    //TODO: this example, do not work
    //TODO make return value
    private void executeSwitcherPlannedTask(PlannedTask body) {
        boardService.makeSwitcherRequest(body.getClientName(), body.getModule(), body.getModuleId());
    }

    //TODO make return value
    private void executeSensorIntervalTask(IntervalTask body) {
        boardService.makeSensorRequest(body.getClientName(), body.getModule(), body.getModuleId());
    }
}
