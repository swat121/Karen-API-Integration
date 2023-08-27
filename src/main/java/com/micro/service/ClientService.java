package com.micro.service;

import com.micro.dto.Client;
import com.micro.enums.Services;
import com.micro.exception.ApiRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ConnectionService connectionService;
    @Getter
    private String clientEndPoint;

    public boolean isOldClientInDb(Client client) {
        Client oldClient = connectionService.getResponseFromService(Services.KAREN_DATA.getTitle(), "/api/v1/clients/" + client.getName(), Client.class);
        clientEndPoint = (oldClient != null) ? "/client/update" : "/clients";
        return oldClient != null &&
                oldClient.getSsid().equals(client.getSsid()) &&
                oldClient.getIp().equals(client.getIp()) &&
                oldClient.getMac().equals(client.getMac());
    }

    public String getIpOfAvailableClient(String name) {
        Client client = connectionService.getResponseFromService(Services.KAREN_DATA.getTitle(), "/ap1/v1/clients/" + name, Client.class);
        if (client == null) {
            throw new ApiRequestException(String.format("Client not found: [%s]", name));
        }
        return client.getIp();
    }
}
