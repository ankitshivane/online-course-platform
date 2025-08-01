package com.course.platform.profile.practice;

import org.springframework.stereotype.Component;

public class FeatureService {
    private String message;

    public FeatureService(){

    }

    public FeatureService(String message){
        this.message=message;
    }
    public String describe(){
        return this.message;
    }
}
