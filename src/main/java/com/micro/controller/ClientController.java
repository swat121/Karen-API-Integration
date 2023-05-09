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
    private final ConnectionService connectionService;
    private final ClientService clientService;

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
        if (clientService.isClientInDb(client)) {
            connectionService.postRequestForService(Services.KAREN_BOT.getTitle(), "/api/v1/bot/client/connect", request);
            return "Data has not update because old data equals new data";
        }
        connectionService.postRequestForService(Services.KAREN_BOT.getTitle(), "/api/v1/bot" + clientService.getClientEndPoint(), request);
        return connectionService.postRequestForService(Services.KAREN_DATA.getTitle(), clientService.getClientEndPoint(), request);
    }
}
