package com.micro.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "feignClient", url = "192.168.0.100:80")
public interface NodeMCU_Garry {
    @GetMapping("/temperature")
    String getRequest();
}
