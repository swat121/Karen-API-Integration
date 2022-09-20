package com.micro.service;

import com.micro.config.BotConfig;
import com.micro.model.BotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class BotService {
    private final BotConfig botConfig;
    private final RestTemplate restTemplate;
    private final String urlTelegram = "https://api.telegram.org/bot";
    public String sendMessage(String message){
        BotResponse botResponse = restTemplate.getForEntity(urlTelegram +
                botConfig.getToken() +
                "/sendMessage?chat_id=" +
                botConfig.getChat()+
                "&text="+message,BotResponse.class).getBody();
        return botResponse.getOk();
    }
}
