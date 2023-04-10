package com.micro.controller;

import com.micro.service.ConnectionService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ClientController {
    private static final Logger LOG = LogManager.getRootLogger();
    private final ConnectionService connectionService;

    @GetMapping("/ping")
    public String ping() {
        LOG.info("======================== ConnectController: GetMapping - ping ========================");
        return "pong";
    }

    @SneakyThrows
    @PostMapping("/clients")
    public String addIpAddress(@RequestParam(value = "ip", required = true) String ip, @RequestParam(value = "name", required = true) String name) {
        LOG.info("======================== ConnectController: PostMapping - addIpAddress ========================");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", name);
        map.add("ip", ip);
        map.add("mac", ip);
        map.add("s", ip);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        return connectionService.postRequestForService("karen-data", "/clients", request);
    }
}
