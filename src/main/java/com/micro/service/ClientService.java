package com.micro.service;

import com.micro.dto.Client;
import com.micro.enums.Services;
import com.micro.exception.ApiRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ConnectionService connectionService;
    //TODO заменить endpoint /api/v1/bot/client/connect -> /api/v1/bot/client/connected
    @Getter
    private String clientEndPointForKarenBot = "/api/v1/bot/client/connect";

    private HttpEntity<Client> makeClientRequest(Client client) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(client, headers);
    }

    public void checkClientAlreadyExist(Client client) {
        Client clientData = connectionService.getResponseFromService(Services.KAREN_DATA.getTitle(), "/api/v1/clients/" + client.getName(), Client.class);
        if (clientData == null) {
            String response = connectionService.postRequestForService(Services.KAREN_DATA.getTitle(), "/api/v1/clients", makeClientRequest(client));
            clientEndPointForKarenBot = "/api/v1/clients";
        } else {
            List<String> differences = clientData.getDifferences(client);
            if(differences != null) {
                //TODO add log
                connectionService.putRequestForService(Services.KAREN_DATA.getTitle(), "/api/v1/client/update", makeClientRequest(client));
                clientEndPointForKarenBot = "/api/v1/client/update";
            }
        }
        connectionService.postRequestForService(Services.KAREN_BOT.getTitle(), clientEndPointForKarenBot, makeClientRequest(client));
    }
}
