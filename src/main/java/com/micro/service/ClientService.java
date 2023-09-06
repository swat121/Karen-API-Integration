package com.micro.service;

import com.micro.dto.Client;
import com.micro.dto.ExternalUser;
import com.micro.dto.board.BoardConfig;
import com.micro.dto.bot.NotifyRequest;
import com.micro.enums.Services;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private static final String KAREN_DATA = Services.KAREN_DATA.getTitle();
    private static final String KAREN_BOT = Services.KAREN_BOT.getTitle();
    private static final String API_V1_BOARDS = "/api/v1/boards";

    private static final String API_V1_CLIENTS = "/api/v1/clients";
    private static final String API_V1_CLIENT_UPDATE = "/api/v1/client/update";
    private static final String API_V1_BOT_NOTIFY = "/api/v1/bot/notify";
    private static final String API_V1_USERS = "/api/v1/users";
    private final ConnectionService connectionService;

    private String requestMessage;

    private <T> HttpEntity<T> buildRequest(T data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(data, headers);
    }

    public String postBoardConfigInKarenData(BoardConfig boardConfig) {
        return connectionService.postRequestForService(KAREN_DATA, API_V1_BOARDS, buildRequest(boardConfig));
    }

    public void checkAndProcessClient(Client client) {
        requestMessage = String.format("Client was connected: %s, ip: %s", client.getName(), client.getIp());
        Client existingClient = getClient(client.getName());
        if (existingClient == null) {
            createClient(client);
        } else {
            updateClientIfNeeded(existingClient, client);
        }
        notifyKarenBot();
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

    private void notifyKarenBot() {
        connectionService.postRequestForService(KAREN_BOT, API_V1_BOT_NOTIFY, buildRequest(NotifyRequest
                .builder()
                .message(requestMessage)
                .telegramIds(getIsNotifyUsers())
                .build()));
    }

    private List<ExternalUser> getIsNotifyUsers() {
        return Arrays.stream(connectionService.getResponseFromService(KAREN_DATA, API_V1_USERS, ExternalUser[].class)).filter(ExternalUser::getIsNotify).toList();
    }
}
