package com.micro.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "feignClient", url = "192.168.0.100:80")
public interface NodeMCU_Garry {
    @GetMapping("/temperature")
    String getTemperature();
    @GetMapping("/setting?{key}={value}")
    String setting(@PathVariable(value = "key") String key, @PathVariable(value = "value") String value);
}
