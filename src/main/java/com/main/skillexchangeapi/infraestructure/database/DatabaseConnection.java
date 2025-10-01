package com.main.skillexchangeapi.infraestructure.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConnection {
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
