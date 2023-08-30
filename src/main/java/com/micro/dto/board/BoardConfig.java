package com.micro.dto.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(onConstructor_ = {@JsonCreator})
public class BoardConfig {

    @NonNull
    @JsonProperty("name")
    private String name;

    private Setting setting;
}
