package com.main.skillexchangeapi.app.config;

import com.main.skillexchangeapi.apirest.abstractions.DatasourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevDataSourceConfig implements DatasourceConfig {
    @Value("${info.app.name}")
    private String appName;

    public String getAppName() {
        return appName;
    }

    @Override
    public void setup() {
        System.out.println("Iniciando REST en dev");
    }
}
