package com.micro.controller;


import com.micro.service.Service;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class MainController {

    public Service service;

    @SneakyThrows
    @GetMapping("/{name}/{key}")
    public String data(@PathVariable(value = "key") String key, @PathVariable(value = "name") String name) {
        return service.request(name, key);
    }

    @GetMapping("/{name}/message/{text}")
    public String message(@PathVariable(value = "text") String text, @PathVariable(value = "name") String name) {
        return service.message(name, "message", text);
    }
    @GetMapping("/{name}/setting/{key}")
    public String setting(@PathVariable(value = "name") String name, @PathVariable(value = "key") String key){
        return service.setting(name,key);
    }
    @GetMapping("/{name}/sensor/{key}")
    public String sensor(@PathVariable(value = "name") String name, @PathVariable(value = "key") String key){
        return service.sensor(name,key);
    }
    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }
}

