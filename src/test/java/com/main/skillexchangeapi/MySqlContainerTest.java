package com.main.skillexchangeapi;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class MySqlContainerTest {
    @Container
    private static final MySQLContainer mySqlContainer = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("skill_exchange_dev")
            .withUsername("skilleduser")
            .withPassword("piero1496");
    int port = mySqlContainer.getMappedPort(3306);

    String jdbcUrl = mySqlContainer.getJdbcUrl();

    @DynamicPropertySource
    private static void setupProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySqlContainer::getUsername);
        registry.add("spring.datasource.password", mySqlContainer::getPassword);
    }


}
