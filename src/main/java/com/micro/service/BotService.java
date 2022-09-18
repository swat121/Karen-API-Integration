package com.micro.service;

import com.micro.config.UrlConfig;
import com.micro.model.BotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BotService {
    private final UrlConfig urlConfig;
    private final RestTemplate restTemplate;
    private String urlTelegram = "https://api.telegram.org/bot";

    public String sendMessage(String message){
        BotResponse botResponse = restTemplate.getForEntity(urlTelegram+urlConfig.getBot().get("test").get("token") +
                "/sendMessage?chat_id="+urlConfig.getBot().get("test").get("chatId")+
                "&text="+message,BotResponse.class).getBody();
        return botResponse.getOk();
    }
}
