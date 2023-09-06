package com.micro.controller;

import com.micro.dto.Client;
import com.micro.dto.board.BoardConfig;
import com.micro.service.ClientService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ClientController {
    private static final Logger LOG = LogManager.getRootLogger();
    private final ClientService clientService;

    @PostMapping("/api/v1/clients")
    public void setClient(@RequestBody Client client) {
        clientService.checkAndProcessClient(client);
    }

    @PostMapping("/api/v1/board-config")
    public String setBoardConfig(@RequestBody BoardConfig boardConfig) {
        return clientService.postBoardConfigInKarenData(boardConfig);
    }
}
