package com.main.skillexchangeapi.repositories;

import com.main.skillexchangeapi.domain.abstractions.repositories.ICategoriaRepository;
import com.main.skillexchangeapi.domain.entities.Categoria;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class CategoriaRepositoryTester extends MySQLContainer<CategoriaRepositoryTester> {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Autowired
    private ICategoriaRepository repository;

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
    void checkCategoriasExists() throws DatabaseNotWorkingException, ResourceNotFoundException {
        List<Categoria> categorias = repository.obtener();
        Assertions.assertFalse(categorias.isEmpty(), "Existen " + categorias.size() + " categoría(s)");
    }
}
