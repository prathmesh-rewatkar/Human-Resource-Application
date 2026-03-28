package com.example.HumanResourceApplication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;

@Configuration
public class ValidationConfig {

    @Autowired
    private Validator validator;

    @Autowired
    public void configureValidatingRepositoryEventListener(
            ValidatingRepositoryEventListener listener) {

        listener.addValidator("beforeCreate", validator);
        listener.addValidator("beforeSave", validator);
    }
}