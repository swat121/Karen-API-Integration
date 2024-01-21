package com.micro.dto.board;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.Data;

import java.util.List;

@Data
@RequiredArgsConstructor(onConstructor_ = {@JsonCreator})
@NoArgsConstructor
public class Device {

    @NonNull
    @JsonProperty("moduleName")
    private String moduleName;
    private List<com.micro.dto.board.Data> data;

}
