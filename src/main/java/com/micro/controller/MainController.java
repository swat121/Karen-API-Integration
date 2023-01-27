package com.micro.controller;


import com.micro.service.MicroControllerService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class MainController {

    public MicroControllerService microControllerService;

    @SneakyThrows
    @GetMapping("/{name}/{key}")
    public String data(@PathVariable(value = "key") String key, @PathVariable(value = "name") String name) {
        return microControllerService.request(name, key);
    }

    @GetMapping("/{name}/message/{text}")
    public String message(@PathVariable(value = "text") String text, @PathVariable(value = "name") String name) {
        return microControllerService.message(name, "message", text);
    }
    @GetMapping("/{name}/setting/{key}")
    public String setting(@PathVariable(value = "name") String name, @PathVariable(value = "key") String key){
        return microControllerService.setting(name,key);
    }
    @GetMapping("/{name}/sensor/{key}")
    public String sensor(@PathVariable(value = "name") String name, @PathVariable(value = "key") String key){
        return microControllerService.sensor(name,key);
    }
    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }
}

