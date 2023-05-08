package com.micro.service;

public enum Services {
    KAREN_DATA("karen-data"),
    KAREN_BOT("karenbot"),
    ;

    private final String title;

    Services(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
