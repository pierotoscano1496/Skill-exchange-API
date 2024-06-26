package com.main.skillexchangeapi.properties;

import com.main.skillexchangeapi.app.config.DevDataSourceConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class CheckPropertyExist {
    @Value("${info.app.name}")
    private String appName;

    @BeforeEach
    public void beforeEach() {
        System.out.println("Testing properties in " + appName);
    }

    @Test
    public void verifyProperty() {
    }
}
