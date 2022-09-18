package com.micro.controller;


import com.micro.service.BotService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BotController {

    private BotService botService;

    @GetMapping("/bot/{message}")
    public String sendMessage(@PathVariable(value = "message") String message){
        return botService.sendMessage(message);
    }
}
