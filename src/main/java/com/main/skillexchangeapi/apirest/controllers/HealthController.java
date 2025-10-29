package com.main.skillexchangeapi.apirest.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private DatabaseConnection databaseConnection;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AmazonS3 amazonS3;

    @GetMapping
    public Map<String, Object> healthCheck() {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "ok");

        // Verificar MySQL
        try {
            checkMySQLConnection();
            healthStatus.put("mysql", Map.of("status", "up"));
        } catch (Exception e) {
            healthStatus.put("mysql", Map.of("status", "down", "error", e.getMessage()));
            healthStatus.put("status", "degraded");
        }

        // Verificar MongoDB
        try {
            checkMongoDBConnection();
            healthStatus.put("mongodb", Map.of("status", "up"));
        } catch (Exception e) {
            healthStatus.put("mongodb", Map.of("status", "down", "error", e.getMessage()));
            healthStatus.put("status", "degraded");
        }

        // Verificar AWS S3
        try {
            checkS3Connection();
            healthStatus.put("aws_s3", Map.of("status", "up"));
        } catch (Exception e) {
            healthStatus.put("aws_s3", Map.of("status", "down", "error", e.getMessage()));
            healthStatus.put("status", "degraded");
        }

        return healthStatus;
    }

    private void checkMySQLConnection() throws Exception {
        try (Connection connection = databaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT 1");
                ResultSet rs = stmt.executeQuery()) {
            if (!rs.next()) {
                throw new Exception("No se pudo ejecutar query de prueba");
            }
        }
    }

    private void checkMongoDBConnection() throws Exception {
        // Ejecutar una operación simple para verificar la conexión
        mongoTemplate.getDb().getName(); // Esto lanza excepción si no hay conexión
    }

    private void checkS3Connection() throws Exception {
        // Intentar listar buckets para verificar conexión
        amazonS3.listBuckets();
    }
}
