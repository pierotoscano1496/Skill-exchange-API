package com.main.skillexchangeapi.repositories;

import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UsuarioRepositoryTester extends MySQLContainer<UsuarioRepositoryTester> {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Autowired
    private MockMvc mockMvc;

    @Container
    private static final MySQLContainer mySqlContainer = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("skill_exchange_dev")
            .withUsername("skilleduser")
            .withPassword("piero1496")
            .withInitScript("init.sql");
    int port = mySqlContainer.getMappedPort(3306);

    String jdbcUrl = mySqlContainer.getJdbcUrl();

    @DynamicPropertySource
    private static void setupProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySqlContainer::getUsername);
        registry.add("spring.datasource.password", mySqlContainer::getPassword);
    }

    @Test
    void testMySQLContainerIsRunning() {
        System.out.println(mySqlContainer.getJdbcUrl());
        Assertions.assertThat(mySqlContainer.isRunning()).isTrue();
    }

    @Test
    void testTableExists() throws SQLException {
        try (Connection connection = databaseConnection.getConnection();
             ResultSet resultSet = connection.prepareStatement("SHOW TABLES").executeQuery()) {
            int tablesCount = 0;
            while (resultSet.next()) {
                tablesCount++;
            }

            Assertions.assertThat(tablesCount).isGreaterThan(0);
        }
    }

    /*@Test
    public void userRegisteredHasOwn16BytesId() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/usuario")
                .content(asJsonString))
    }

    public static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object)
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

     */
}
