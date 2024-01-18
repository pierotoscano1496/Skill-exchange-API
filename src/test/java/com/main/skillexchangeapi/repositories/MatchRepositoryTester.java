package com.main.skillexchangeapi.repositories;

import com.main.skillexchangeapi.domain.abstractions.repositories.IMatchRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.IUsuarioRepository;
import com.main.skillexchangeapi.domain.entities.*;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import com.main.skillexchangeapi.infraestructure.repositories.MatchRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class MatchRepositoryTester extends MySQLContainer<MatchRepositoryTester> {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Autowired
    private IMatchRepository repository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private MockMvc mockMvc;

    private HashMap<UUID, Categoria> categoriasCollection;
    private HashMap<UUID, ModalidadPago> modalidadesPagoCollection;
    private HashMap<UUID, Plan> planesCollection;
    private HashMap<UUID, Skill> skillsCollection;
    private HashMap<UUID, Usuario> usuariosCollection;

    /**
     * Preparar entorno previo a la creación de matchs
     */
    @BeforeEach
    void setup() {
        /**
         * --- CATEGORÍAS, SUB CATEGORÍAS Y HABILIDADES
         * Diccionarios:
         *  Categorías: música, educación, tecnología
         *  Subcategorías:
         *  - Música: instrumentos musicales, canto, DJ
         *  - Educación: inicial, primaria, secundaria, preuniversitario
         *  - Tecnología: entornos de desarrollo, servicios en nube, lenguajes de programación, Frameworks de desarrollo, bases de datos
         *  Habilidades:
         *  - Instrumentos musicales: guitarra, piano, violín
         *  - Canto: ópera, rap, canto urbano
         *  - DJ: mezclas, remixes
         *  - Inicial: lenguaje, matemática, ciencias naturales
         *  - Primaria: lenguaje, comprensión de lectura, biología, historia, geografía, geometría, aritmética
         *  - Secundaria: lenguaje, literatura, comprensión de lectura, biología, química, física, historia, geografía, geometría, aritmética, trigonometría, álgebra
         *  - Preuniversitario: lenguaje, habilidad verbal, química, física, historia, geografía, geometría, aritmética, trigonometría, álgebra
         *  - Entornos de desarrollo: frontend, backend, desarrollo móvil, fullstack, SAP
         *  - Servicios en nube: AWS, Azure, Google Cloud
         *  - Lenguajes de programación: Java, Python, C++, C#, Go, Javascript, Typescript, Ruby, PHP
         *  - Frameworks de desarrollo: Spring, Django, Flask, .NET, .NET Core, Angular, React, Vue, Next.js, Ruby on rails, Laravel
         *  - Bases de datos: SQL Server, Oracle, MySQL, PostgreSQL, MariaDB, SQLite, procedimientos almacenados, funciones
         */

        Categoria musica = Categoria.builder()
                .nombre("Música")
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                .build();
        Categoria educacion = Categoria.builder()
                .nombre("Educación")
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                .build();
        Categoria habTecnicas = Categoria.builder()
                .nombre("Habilidades técnicas")
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))
                .build();




    }

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

    /*
    @Test
    void testMatchRegistered(){
        // Objetos que componendrán los matchs
        Usuario usuario=new

        Match match=Match.builder()

                .build();
        Match = repository.registrar()
    }

     */
}
