package com.micro.controller;


import com.micro.service.BoardService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
public class BoardController {
    private static final Logger LOG = LogManager.getRootLogger();
    public BoardService boardService;

    @GetMapping("/api/v1/{name}/sensors/{module}/{id}")
    public String getDataBySensor(@PathVariable String name, @PathVariable String module, @PathVariable String id){
        return boardService.makeSensorRequest(name, module, id);
    }

    @GetMapping("/api/v1/{name}/trackers/{module}/{id}")
    public String getDataByTracker(@PathVariable String name, @PathVariable String module, @PathVariable String id){
        return boardService.makeTrackerRequest(name, module, id);
    }

    @PutMapping("/api/v1/{name}/switchers/{module}/{id}")
    public String putDataBySwitcher(@PathVariable String name, @PathVariable String module, @PathVariable String id){
        return boardService.makeSwitcherRequest(name, module, id);
    }
}

