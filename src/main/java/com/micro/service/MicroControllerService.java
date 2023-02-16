package com.micro.service;

import com.micro.config.UrlConfig;
import com.micro.exception.ApiRequestException;
import com.micro.model.DataResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MicroControllerService {
    private final UrlConfig urlConfig;
    private final ConnectionService connectionService;
    private static final Logger LOG = LogManager.getRootLogger();

    public String request(String name, String key) {
        LOG.info("======================== Micro controller service: request - " + name + key + " ========================");
        switch (key) {
            case "help":
                LOG.info("=========== Micro controller service: get " + key + " response from service - " + name + key + " ===========");
                DataResponse dataResponse = connectionService.getResponseFromService(urlConfig.getResource().get(name), key, DataResponse.class);
                return dataResponse.getName() + ": " + dataResponse.getHelp();
            case "status":
                LOG.info("=========== Micro controller service: get " + key + " response from service - " + name + key + " ===========");
                return connectionService.getResponseFromService(urlConfig.getResource().get(name), key, String.class);
            default:
                LOG.info("=========== Micro controller service: get " + key + " command not found ===========");
                throw new ApiRequestException(String.format("Command not found: [%s]", key));
        }
    }

    public String message(String name, String key, String text) {
        LOG.info("======================== Micro controller service: message " + key + " response from service - " + name + key + " ========================");
        DataResponse dataResponse = connectionService.getResponseFromService(urlConfig.getResource().get(name), key + "?text=" + text, DataResponse.class);
        return dataResponse.getMessage();
    }

    public String setting(String name, String key) {
        LOG.info("======================== Micro controller service: setting - " + name + key + " ========================");
        DataResponse dataResponse = connectionService.getResponseFromService(urlConfig.getResource().get(name), key, DataResponse.class);
        return switch (key) {
            case "relay1" -> dataResponse.getRelay1();
            case "relay2" -> dataResponse.getRelay2();
            case "relay3" -> dataResponse.getRelay3();
            case "backlight" -> dataResponse.getBacklight();
            default -> throw new ApiRequestException(String.format("Command not found: [%s]", key));
        };
    }

    public String sensor(String name, String key) {
        LOG.info("======================== Micro controller service: sensor - " + name + key + " ========================");
        DataResponse dataResponse = connectionService.getResponseFromService(urlConfig.getResource().get(name), key, DataResponse.class);
        return switch (key) {
            case "temperature" -> dataResponse.getTemperature();
            case "light" -> dataResponse.getLight();
            default -> throw new ApiRequestException("Opps");
        };
    }
}
