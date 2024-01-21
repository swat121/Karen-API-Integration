package com.micro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.dto.Client;
import com.micro.dto.board.BoardConfig;
import com.micro.enums.Services;
import com.micro.exception.ApiRequestException;
import com.micro.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    @Value("${board.port}")
    private String port;
    private static final Logger LOG = LogManager.getRootLogger();
    private static final String KAREN_DATA = Services.KAREN_DATA.getTitle();
    private static final String API_V1_BOARDS = "/api/v1/boards";
    private static final String API_V1_CLIENTS = "/api/v1/clients";
    private static final String API_V1_CLIENT_UPDATE = "/api/v1/client/update";
    private final ConnectionService connectionService;
    private final BotService botService;

    private String requestMessage;

    private <T> HttpEntity<T> buildRequest(T data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(data, headers);
    }

    public void postBoardConfigInKarenData(String ip) {
        String baseUrl = String.format("http://%s:%s/api/v1/board-config", ip, port);
        String response = connectionService.requestForBoard(baseUrl, HttpMethod.GET, null, String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            BoardConfig boardConfig = mapper.readValue(response, BoardConfig.class);
            connectionService.postRequestForService(KAREN_DATA, API_V1_BOARDS, buildRequest(boardConfig));
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

    }

    public void checkAndProcessClient(Client client) {
        requestMessage = String.format("Client was connected: %s, ip: %s", client.getName(), client.getIp());
        try {
            Client existingClient = getClient(client.getName());
            updateClientIfNeeded(existingClient, client);
        } catch (ApiRequestException e) {
            if (e.getErrorCode() == ErrorCode.ENTITY_NOT_FOUND) {
                createClient(client);
            } else {
                throw e;
            }
        }
        botService.notifyKarenBot(requestMessage, false);
    }

    public Client getClient(String name) {
        return connectionService.getResponseFromService(KAREN_DATA, API_V1_CLIENTS + "/" + name, Client.class);
    }

    private void createClient(Client client) {
        connectionService.postRequestForService(KAREN_DATA, API_V1_CLIENTS, buildRequest(client));
        requestMessage = String.format("New client was added: %s, ip: %s", client.getName(), client.getIp());
    }

    private void updateClientIfNeeded(Client existingClient, Client client) {
        List<String> differences = existingClient.getDifferences(client);
        if (differences.size() != 0) {
            connectionService.putRequestForService(KAREN_DATA, API_V1_CLIENT_UPDATE, buildRequest(client));
            requestMessage = String.format("Client was updated: %s, ip: %s, differences fields: %s", client.getName(), client.getIp(), differences);
        }
    }
}
