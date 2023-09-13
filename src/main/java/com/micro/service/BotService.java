package com.micro.service;

import com.micro.dto.ExternalUser;
import com.micro.dto.bot.NotifyRequest;
import com.micro.enums.Services;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BotService {
    private static final String API_V1_USERS = "/api/v1/users";
    private static final String API_V1_BOT_NOTIFY = "/api/v1/bot/notify";
    private static final String KAREN_BOT = Services.KAREN_BOT.getTitle();
    private static final String KAREN_DATA = Services.KAREN_DATA.getTitle();
    private final ConnectionService connectionService;

    private <T> HttpEntity<T> buildRequest(T data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(data, headers);
    }

    public void notifyKarenBot(String message, boolean isNotifyAllUsers) {
        List<ExternalUser> users = isNotifyAllUsers ? getUsers() : getIsNotifyUsers();
        connectionService.postRequestForService(KAREN_BOT, API_V1_BOT_NOTIFY, buildRequest(NotifyRequest
                .builder()
                .message(message)
                .telegramIds(users)
                .build()));
    }

    private List<ExternalUser> getIsNotifyUsers() {
        return Arrays.stream(connectionService.getResponseFromService(KAREN_DATA, API_V1_USERS, ExternalUser[].class)).filter(ExternalUser::getIsNotify).toList();
    }

    private List<ExternalUser> getUsers() {
        return List.of(connectionService.getResponseFromService(KAREN_DATA, API_V1_USERS, ExternalUser[].class));
    }
}
