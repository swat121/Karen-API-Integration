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
public class BoardController {
    private static final Logger LOG = LogManager.getRootLogger();
    public MicroControllerService microControllerService;

    @GetMapping("/api/v1/{name}/sensors/{module}/{id}")
    public String getDataBySensor(@PathVariable String name, @PathVariable String module, @PathVariable String id){
        return microControllerService.makeSensorRequest(name, module, id);
    }

//    @SneakyThrows
//    @GetMapping("/{name}/{key}")
//    public String data(@PathVariable(value = "key") String key, @PathVariable(value = "name") String name) {
//        LOG.info("======================== MainController: GetMapping - " + name + " | " + key + " ========================");
//        return microControllerService.request(name, key);
//    }
//
//    @GetMapping("/{name}/message/{text}")
//    public String message(@PathVariable(value = "text") String text, @PathVariable(value = "name") String name) {
//        LOG.info("======================== MainController: GetMapping - message " + name + " ========================");
//        return microControllerService.message(name, "message", text);
//    }
//    @GetMapping("/{name}/setting/{key}")
//    public String setting(@PathVariable(value = "name") String name, @PathVariable(value = "key") String key){
//        LOG.info("======================== MainController: GetMapping - setting " + name + " | " + key + " ========================");
//        return microControllerService.setting(name,key);
//    }
//    @GetMapping("/{name}/sensor/{key}")
//    public String sensor(@PathVariable(value = "name") String name, @PathVariable(value = "key") String key){
//        LOG.info("======================== MainController: GetMapping - sensor " + name + " | " + key + " ========================");
//        return microControllerService.sensor(name,key);
//    }
}

