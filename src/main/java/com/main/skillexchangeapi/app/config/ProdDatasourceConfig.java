package com.main.skillexchangeapi.app.config;

import com.main.skillexchangeapi.apirest.abstractions.DatasourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdDatasourceConfig implements DatasourceConfig {
    @Override
    public void setup() {
        System.out.println("Iniciando en producción");
    }
}
