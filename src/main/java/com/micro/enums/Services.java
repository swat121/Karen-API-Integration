package com.micro.enums;

import lombok.Getter;

@Getter
public enum Services {
    KAREN_DATA("8085"),
    KAREN_BOT("8084");

    private final String title;

    Services(String title) {
        this.title = title;
    }

}
