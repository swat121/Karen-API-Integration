package com.micro.service;

import com.micro.dto.Client;
import com.micro.enums.Services;
import com.micro.exception.ApiRequestException;
import com.micro.model.DataResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MicroControllerService {
    private final ConnectionService connectionService;
    private final ClientService clientService;
    private static final Logger LOG = LogManager.getRootLogger();

    public String request(String name, String key) {
        LOG.info("======================== Micro controller service: request - " + name + " | " + key + " ========================");
        String ip = clientService.getIpOfAvailableClient(name);
        switch (key) {
            case "help":
                LOG.info("=========== Micro controller service: get " + key + " response from service - " + name + " | " + key + " ===========");
                DataResponse dataResponse = connectionService.getResponseFromMicro(ip + ":80/", key, DataResponse.class);
                return dataResponse.getName() + ": " + dataResponse.getHelp();
            case "status":
                LOG.info("=========== Micro controller service: get " + key + " response from service - " + name + " | " + key + " ===========");
                return connectionService.getResponseFromMicro(ip + ":80/", key, String.class);
            default:
                LOG.info("=========== Micro controller service: get " + key + " command not found ===========");
                throw new ApiRequestException(String.format("Command not found: [%s]", key));
        }
    }

    public String message(String name, String key, String text) {
        LOG.info("======================== Micro controller service: message " + key + " response from service - " + name + " | " + key + " ========================");
        String ip = clientService.getIpOfAvailableClient(name);
        DataResponse dataResponse = connectionService.getResponseFromMicro(ip + ":80/", key + "?text=" + text, DataResponse.class);
        return dataResponse.getMessage();
    }

    public String setting(String name, String key) {
        LOG.info("======================== Micro controller service: setting - " + name + " | " + key + " ========================");
        String ip = clientService.getIpOfAvailableClient(name);
        DataResponse dataResponse = connectionService.getResponseFromMicro(ip + ":80/", key, DataResponse.class);
        return switch (key) {
            case "relay1" -> dataResponse.getRelay1();
            case "relay2" -> dataResponse.getRelay2();
            case "relay3" -> dataResponse.getRelay3();
            case "backlight" -> dataResponse.getBacklight();
            default -> throw new ApiRequestException(String.format("Command not found: [%s]", key));
        };
    }

    public String sensor(String name, String key) {
        LOG.info("======================== Micro controller service: sensor - " + name + " | " + key + " ========================");
        String ip = clientService.getIpOfAvailableClient(name);
        DataResponse dataResponse = connectionService.getResponseFromMicro(ip + ":80/", key, DataResponse.class);
        return switch (key) {
            case "temperature" -> dataResponse.getTemperature();
            case "light" -> dataResponse.getLight();
            default -> throw new ApiRequestException("Opps");
        };
    }
}
