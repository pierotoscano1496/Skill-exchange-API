package com.main.skillexchangeapi.app.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CoomandLineAppStartupRunner implements CommandLineRunner {
    /*
    @Value("${url.allowed.host}")
    private String allowedHost;
    */

    @Value("${url.server}")
    private String urlServer;

    @Override
    public void run(String... args) throws Exception {
        try {
            //System.out.println("Allowed URL for consume: " + allowedHost);
            System.out.println("Servidor corriendo en: " + urlServer + "/api/v1/");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
