package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.ICategoriaRepository;
import com.main.skillexchangeapi.domain.entities.Categoria;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CategoriaRepository implements ICategoriaRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public Categoria obtenerById(UUID id) {
        return null;
    }

    @Override
    public List<Categoria> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL obtener_categorias()}");
             ResultSet resultSet = statement.executeQuery()) {
            List<Categoria> categorias = new ArrayList<>();

            while (resultSet.next()) {
                categorias.add(Categoria.builder()
                        .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                        .nombre(resultSet.getString("NOMBRE"))
                        .build());
            }

            if (!categorias.isEmpty()) {
                return categorias;
            } else {
                throw new ResourceNotFoundException("No existen categorías");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se pudieron buscar los planes");
        }
    }
}
