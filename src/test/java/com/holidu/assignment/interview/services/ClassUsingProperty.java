package com.holidu.assignment.interview.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClassUsingProperty {
     
    @Value("${app.datasource.endpoint}")
    private String url;
     
    public String retrieveURL() {
        return url;
    }
}

