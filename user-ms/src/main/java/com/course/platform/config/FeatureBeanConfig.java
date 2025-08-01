package com.course.platform.config;

import com.course.platform.profile.practice.FeatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
public class FeatureBeanConfig {

    @Bean
    @Profile("dev")
    public FeatureService devFeatureService(){
        log.info("Dev profile is activated");
        return new FeatureService("Dev Feature Implementation");
    }

    @Bean
    @Profile("test")
    public FeatureService testFeatureService(){
        log.info("Test profile is activated");
        return new FeatureService("Test Feature Implementation");
    }

    @Bean
    @Profile("prod")
    public FeatureService prodFeatureService(){
        log.info("Prod profile is activated");
        return new FeatureService("Prod Feature Implementation");
    }

    @Bean
    @Profile("!dev & !test & !prod")
    public FeatureService defaultFeatureService(){
        log.info("Default profile is activated");
        return new FeatureService("DEFAULT Feature Implementation");
    }

}
