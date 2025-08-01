package com.course.platform.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile({"dev","test"})
@Slf4j
public class DevTestOnlyController {
    public DevTestOnlyController(){
      log.info("DevTestOnlyController Bean is loaded only for dev environment..");
    }
    @GetMapping("/dev-info")
    public String devInfo() {
        return "Visible only in DEV OR Test profile";
    }
}
