package com.main.skillexchangeapi.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IUsuarioRepository;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import com.main.skillexchangeapi.infraestructure.repositories.UsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
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
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UsuarioRepositoryTester extends MySQLContainer<UsuarioRepositoryTester> {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Autowired
    private IUsuarioRepository repository;

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

    @Test
    public void userRegisteredHasOwn16BytesId() throws DatabaseNotWorkingException, EncryptionAlghorithmException, NotCreatedException {
        Usuario usuario = Usuario.builder()
                .id(null)
                .dni("98765432")
                .carnetExtranjeria(null)
                .tipoDocumento("dni")
                .correo("yop@mail.com")
                .nombres("Juan")
                .apellidos("Perez")
                .fechaNacimiento(Date.valueOf("1990-10-11").toLocalDate())
                .perfilLinkedin("linkedin.com")
                .perfilFacebook("facebook.com")
                .perfilInstagram("ig.com")
                .perfilTiktok("tiktok.com")
                .clave("clave123")
                .build();

        Usuario usuarioRegistered = repository.registrar(usuario);
        System.out.println(usuarioRegistered.getId());

        byte[] userIdBytes = UuidManager.UuidToBytes(usuarioRegistered.getId());

        Assert.assertEquals(16, userIdBytes.length);
    }

    /*
    public static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object)
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

     */
}
