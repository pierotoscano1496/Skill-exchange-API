package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.ITestingRepository;
import com.main.skillexchangeapi.domain.entities.TestingModel;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Repository
public class TestingRepository implements ITestingRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public TestingModel registrar(TestingModel testingModel) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_testing_tabla(?, ?)}")) {
            statement.setObject("p_id", UuidManager.randomUuidToBytes());
            statement.setString("p_nombre", testingModel.getNombre());

            try (ResultSet resultSet = statement.executeQuery()) {
                TestingModel testingModelRegistered = null;

                while (resultSet.next()) {
                    testingModelRegistered = TestingModel.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .nombre(resultSet.getString("NOMBRE"))
                            .build();

                    break;
                }

                if (testingModelRegistered != null) {
                    return testingModelRegistered;
                } else {
                    throw new NotCreatedException("No se creó el modelo");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se creó el modelo");
        }
    }

    @Override
    public TestingModel obtener(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL obtener_testing_tabla(?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(id));

            try (ResultSet resultSet = statement.executeQuery()) {
                TestingModel testingModel = null;

                while (resultSet.next()) {
                    testingModel = TestingModel.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .nombre(resultSet.getString("NOMBRE"))
                            .build();

                    break;
                }

                if (testingModel != null) {
                    return testingModel;
                } else {
                    throw new ResourceNotFoundException("No se obtuvo el modelo");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se obtuvo el modelo");
        }
    }
}
