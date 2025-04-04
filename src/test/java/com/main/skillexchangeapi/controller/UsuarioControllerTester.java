package com.main.skillexchangeapi.controller;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.application.services.UsuarioService;
import com.main.skillexchangeapi.domain.abstractions.repositories.IUsuarioRepository;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioControllerTester {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private IUsuarioRepository repository;

    @LocalServerPort
    private int randomServerPort;

    @Container
    private static final MySQLContainer mySqlContainer = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("skill_exchange_dev")
            .withUsername("skilleduser")
            .withPassword("piero1496")
            .withInitScript("init.sql");

    /*@BeforeAll
    static void prepareContainer(@Autowired IUsuarioRepository repository) throws DatabaseNotWorkingException {
        mySqlContainer.e;
    }*/

    @Test
    void registrarShouldGiveUUID() throws URISyntaxException {
        try {
            final String urlBase = "http://backend:" + randomServerPort + "/api/usuario";

            URI uri = new URI(urlBase);

            Usuario usuario = Usuario.builder()
                    .id(null)
                    .dni("98765451")
                    .carnetExtranjeria("")
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

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Usuario> request = new HttpEntity<>(usuario, headers);

            ResponseEntity<Usuario> response = testRestTemplate.postForEntity(uri, request, Usuario.class);
            //response.getStatusCode();
            Usuario usuarioRegistered = response.getBody();

            Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
            Assertions.assertEquals(UuidManager.UuidToBytes(usuarioRegistered.getId()).length, 16);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
