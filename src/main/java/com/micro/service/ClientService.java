package com.micro.service;

import com.micro.dto.Client;
import com.micro.enums.Services;
import com.micro.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ConnectionService connectionService;
    private String clientEndPoint;

    public boolean isClientInDb(Client client) {
        Client oldClient = connectionService.getResponseFromService(Services.KAREN_DATA.getTitle(), "/clients/" + client.getName(), Client.class);
        clientEndPoint = (oldClient != null) ? "/client/update" : "/clients";
        return oldClient != null &&
                oldClient.getSsid().equals(client.getSsid()) &&
                oldClient.getIp().equals(client.getIp()) &&
                oldClient.getMac().equals(client.getMac());
    }

    public String getClientEndPoint() {
        return clientEndPoint;
    }

    public String getIpOfAvailableClient(String name) {
        Client client = connectionService.getResponseFromService(Services.KAREN_DATA.getTitle(), "/clients/" + name, Client.class);
        if (client == null) {
            throw new ApiRequestException(String.format("Client not found: [%s]", name));
        }
        return client.getIp();
    }
}
