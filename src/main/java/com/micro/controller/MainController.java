package com.micro.controller;


import com.micro.service.MicroControllerService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class MainController {
    private static final Logger LOG = LogManager.getRootLogger();
    public MicroControllerService microControllerService;

    @SneakyThrows
    @GetMapping("/{name}/{key}")
    public String data(@PathVariable(value = "key") String key, @PathVariable(value = "name") String name) {
        LOG.info("======================== MainController: GetMapping - " + name + key + " ========================");
        return microControllerService.request(name, key);
    }

    @GetMapping("/{name}/message/{text}")
    public String message(@PathVariable(value = "text") String text, @PathVariable(value = "name") String name) {
        LOG.info("======================== MainController: GetMapping - message " + name + " ========================");
        return microControllerService.message(name, "message", text);
    }
    @GetMapping("/{name}/setting/{key}")
    public String setting(@PathVariable(value = "name") String name, @PathVariable(value = "key") String key){
        LOG.info("======================== MainController: GetMapping - setting " + name + key + " ========================");
        return microControllerService.setting(name,key);
    }
    @GetMapping("/{name}/sensor/{key}")
    public String sensor(@PathVariable(value = "name") String name, @PathVariable(value = "key") String key){
        LOG.info("======================== MainController: GetMapping - sensor " + name + key + " ========================");
        return microControllerService.sensor(name,key);
    }
    @GetMapping("/ping")
    public String ping(){
        LOG.info("======================== MainController: GetMapping - ping ========================");
        return "pong";
    }
}

