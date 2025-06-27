package com.main.skillexchangeapi.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private Environment environment;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] activeProfiles = environment.getActiveProfiles();
        boolean isProd = false;

        for (String profile : activeProfiles) {
            if ("prod".equals(profile)) {
                isProd = true;
                break;
            }
        }

        String[] allowedOrigins;
        if (isProd) {
            allowedOrigins = new String[] { "http://tuchambita.com/" };
        } else {
            allowedOrigins = new String[] { "http://localhost:3000" };
        }

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowCredentials(false)
                .maxAge(3600)
                .allowedHeaders("Accept", "Content-Type", "Origin", "Authorization", "X-Auth-Token")
                .exposedHeaders("X-Auth-Token", "Authorization")
                .allowedMethods("POST", "GET", "DELETE", "PUT", "OPTIONS");
    }
}
