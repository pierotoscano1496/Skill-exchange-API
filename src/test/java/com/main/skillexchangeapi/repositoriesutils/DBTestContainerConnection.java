package com.main.skillexchangeapi.repositoriesutils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@TestConfiguration
public class DBTestContainerConnection {
    @Value("${spring.datasource.url}")
    private String dataBaseUrl;

    @Value("${spring.datasource.username}")
    private String dataBaseUser;

    @Value("${spring.datasource.password}")
    private String dataBasePassword;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dataBaseUrl, this.dataBaseUser, this.dataBasePassword);
    }
}
