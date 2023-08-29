package com.micro.controller;

import com.micro.dto.Client;
import com.micro.service.ClientService;
import com.micro.service.ConnectionService;
import com.micro.enums.Services;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ClientController {
    private static final Logger LOG = LogManager.getRootLogger();
    private final ClientService clientService;

    @SneakyThrows
    @PostMapping("/api/v1/clients")
    public void setClient(@RequestBody Client client) {
        clientService.checkClientAlreadyExist(client);
    }
}
