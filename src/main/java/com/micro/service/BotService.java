package com.micro.service;

import com.micro.config.BotConfig;
import com.micro.model.BotResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class BotService {
    private final BotConfig botConfig;
    private final RestTemplate restTemplate;
    private static final Logger LOG = LogManager.getRootLogger();
    private final String urlTelegram = "https://api.telegram.org/bot";
    public String sendMessage(String message){
        LOG.info("======================== Bot service: sendMessage - " + message + " ======================== ");
        BotResponse botResponse = restTemplate.getForEntity(urlTelegram +
                botConfig.getToken() +
                "/sendMessage?chat_id=" +
                botConfig.getChat()+
                "&text="+message,BotResponse.class).getBody();
        LOG.info("======================== Bot service: message send ======================== ");
        return botResponse.getOk();
    }
}
