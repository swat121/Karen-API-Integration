package com.micro.service;

import com.micro.dto.Client;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ConnectionService connectionService;
    private String clientEndPoint;

    public boolean isClientInDb(Client client) {
        Client oldClient = connectionService.getResponseFromService("karen-data", "/clients/" + client.getName(), Client.class);
        clientEndPoint = (oldClient != null) ? "/client/update" : "/clients";
        return oldClient != null &&
                oldClient.getSsid().equals(client.getSsid()) &&
                oldClient.getIp().equals(client.getIp()) &&
                oldClient.getMac().equals(client.getMac());
    }

    public String getClientEndPoint() {
        return clientEndPoint;
    }
}
