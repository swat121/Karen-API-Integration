package com.micro.controller;

import com.micro.dto.Client;
import com.micro.service.ConnectionService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ClientController {
    private static final Logger LOG = LogManager.getRootLogger();
    private final ConnectionService connectionService;

    @GetMapping("/ping")
    public String ping() {
        LOG.info("======================== ConnectController: GetMapping - ping ========================");
        return "pong";
    }

    @SneakyThrows
    @PostMapping("/clients")
    public String addIpAddress(@RequestBody Client client) {
        LOG.info("======================== ConnectController: PostMapping - addIpAddress ========================");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Client> request = new HttpEntity<>(client, headers);
        return connectionService.postRequestForService("karen-data", "/clients", request);
    }
}
