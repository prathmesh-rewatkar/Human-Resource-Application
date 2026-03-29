package com.example.HumanResourceApplication.config;

import com.example.HumanResourceApplication.entity.Region;
import com.example.HumanResourceApplication.event.RegionEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config,
            CorsRegistry cors) {

        config.exposeIdsFor(Region.class);
    }

    @Bean
    public RegionEventHandler regionEventHandler() {
        return new RegionEventHandler();
    }
}