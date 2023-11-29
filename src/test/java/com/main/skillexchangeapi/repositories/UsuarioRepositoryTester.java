package com.main.skillexchangeapi.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest
public class UsuarioRepositoryTester extends MySQLContainer<UsuarioRepositoryTester> {

    @Test
    void testMySQLContainerIsRunning() {
        System.out.println(mySqlContainer.getJdbcUrl());
        Assertions.assertThat(mySqlContainer.isRunning()).isTrue();
    }

    @Test
    public void userRegisteredHasOwn16BytesId(){

    }
}
