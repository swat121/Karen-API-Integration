package com.micro.service;

import com.micro.config.UrlConfig;
import com.micro.exception.ApiRequestException;
import com.micro.model.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MicroControllerService {
    private final UrlConfig urlConfig;
    private final ConnectionService connectionService;

    public String request(String name, String key) {
        switch (key) {
            case "help":
                DataResponse dataResponse = connectionService.getResponseFromService(urlConfig.getResource().get(name), key, DataResponse.class);
                return dataResponse.getName() + ": " + dataResponse.getHelp();
            case "status":
                return connectionService.getResponseFromService(urlConfig.getResource().get(name), key, String.class);
            default:
                throw new ApiRequestException(String.format("Command not found: [%s]", key));
        }
    }

    public String message(String name, String key, String text) {
        DataResponse dataResponse = connectionService.getResponseFromService(urlConfig.getResource().get(name), key + "?text=" + text, DataResponse.class);
        return dataResponse.getMessage();
    }

    public String setting(String name, String key) {
        DataResponse dataResponse = connectionService.getResponseFromService(urlConfig.getResource().get(name), key, DataResponse.class);
        switch (key) {
            case "relay1":
                return dataResponse.getRelay1();
            case "relay2":
                return dataResponse.getRelay2();
            case "relay3":
                return dataResponse.getRelay3();
            case "backlight":
                return dataResponse.getBacklight();
            default:
                throw new ApiRequestException(String.format("Command not found: [%s]", key));
        }
    }

    public String sensor(String name, String key) {
        DataResponse dataResponse = connectionService.getResponseFromService(urlConfig.getResource().get(name), key, DataResponse.class);
        switch (key) {
            case "temperature":
                return dataResponse.getTemperature();
            case "light":
                return dataResponse.getLight();
            default:
                throw new ApiRequestException("Opps");
        }
    }
}
