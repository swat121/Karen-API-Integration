package com.micro.controller;


import com.micro.service.ConnectionService;
import com.micro.enums.Services;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BotController {

    private final ConnectionService connectionService;
    private static final Logger LOG = LogManager.getRootLogger();

    @GetMapping("/api/v1/bot/{message}")
    public String sendMessage(@PathVariable(value = "message") String message) {
        LOG.info("======================== BotController: GetMapping - " + message + " ========================");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(message, headers);
        return connectionService.postRequestForService(Services.KAREN_BOT.getTitle(), "/api/v1/bot/message", request);
    }
}
