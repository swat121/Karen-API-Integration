package com.micro.controller;


import com.micro.service.BotService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BotController {

    private BotService botService;
    private static final Logger LOG = LogManager.getRootLogger();

    @GetMapping("/bot/{message}")
    public String sendMessage(@PathVariable(value = "message") String message){
        LOG.info("======================== BotController: GetMapping - " + message + " ========================");
        return botService.sendMessage(message);
    }
}
