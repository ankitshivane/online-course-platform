package com.course.platform.controller;

import com.course.platform.profile.practice.FeatureService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class ProfileController {

    private final FeatureService featureService;

    @Autowired
    public ProfileController(FeatureService featureService){
        log.info("Constructor injection done for ProfileController");
        this.featureService=featureService;
    }


    @Value("${my.prop.name:some-default}")
    private String propName;

    @Value("${feature.welcome-message}")
    private String welcomeMessage;

    @GetMapping("/printVal")
    @Operation(summary = "Prints the value from property files")
    public String printVal(){
        return propName;
    }

    @GetMapping("profile-info")
    public String profileInfo(){
        return welcomeMessage+" :: "+featureService.describe();
    }


}
