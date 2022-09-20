package com.micro.service;

import com.micro.cipher.AesCipherManager;
import com.micro.model.BotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class BotService {
    private final RestTemplate restTemplate;
    private final String urlTelegram = "https://api.telegram.org/bot";
    private final AesCipherManager aesCipherManager;
    public String sendMessage(String message){
        BotResponse botResponse = restTemplate.getForEntity(urlTelegram +
                aesCipherManager.decrypt("token.key") +
                "/sendMessage?chat_id=" +
                aesCipherManager.decrypt("chat.key")+
                "&text="+message,BotResponse.class).getBody();
        return botResponse.getOk();
    }
}
