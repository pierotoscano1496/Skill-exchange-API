package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IPlanRepository;
import com.main.skillexchangeapi.domain.entities.Plan;
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
public class PlanRepository implements IPlanRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public List<Plan> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL obtener_planes()}");
             ResultSet resultSet = statement.executeQuery()) {
            List<Plan> planes = new ArrayList<>();

            while (resultSet.next()) {

                Plan plan = Plan.builder()
                        .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                        .tipo(resultSet.getString("TIPO"))
                        .isFree(resultSet.getBoolean("IS_FREE"))
                        .codigo(resultSet.getString("CODIGO"))
                        .montoBasico(resultSet.getDouble("MONTO_BASICO"))
                        .build();

                planes.add(plan);
            }

            if (!planes.isEmpty()) {
                return planes;
            } else {
                throw new ResourceNotFoundException("No hay planes");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se pudieron buscar los planes");
        }
    }

    @Override
    public Plan obtenerPlanGratuito() throws DatabaseNotWorkingException, ResourceNotFoundException {
        return obtenerByCodigo("free");
    }

    @Override
    public Plan obtenerByCodigo(String codigo) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL obtener_plan_by_codigo(?)}");) {
            statement.setString("p_codigo", codigo);

            ResultSet resultSet = statement.executeQuery();

            Plan plan = null;

            while (resultSet.next()) {
                plan = Plan.builder()
                        .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                        .tipo(resultSet.getString("TIPO"))
                        .isFree(resultSet.getBoolean("IS_FREE"))
                        .codigo(resultSet.getString("CODIGO"))
                        .montoBasico(resultSet.getDouble("MONTO_BASICO"))
                        .build();

                break;
            }
            resultSet.close();

            if (plan != null) {
                return plan;
            } else {
                throw new ResourceNotFoundException("No existe el plan");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DatabaseNotWorkingException("No se pudo buscar el plan");
        }
    }
}
