package com.micro.dto.bot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.micro.dto.ExternalUser;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor(onConstructor_ = {@JsonCreator})
public class NotifyRequest {
    @NonNull
    @JsonProperty("message")
    String message;

    @NonNull
    @JsonProperty("telegramIds")
    List<String> telegramIds;
}
