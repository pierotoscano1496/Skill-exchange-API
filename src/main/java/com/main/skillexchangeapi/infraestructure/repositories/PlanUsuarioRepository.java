package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.domain.abstractions.repositories.IPlanUsuarioRepository;
import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class PlanUsuarioRepository implements IPlanUsuarioRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public PlanUsuario registrar(PlanUsuario planUsuario) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_plan_usuario(?, ?, ?, ?, ?)}");) {
            statement.setLong("p_id_plan", planUsuario.getPlan().getId());
            statement.setLong("p_id_usuario", planUsuario.getUsuario().getId());
            statement.setBoolean("p_is_active", planUsuario.isActive());
            statement.setDouble("p_monto", planUsuario.getMonto());
            statement.setString("p_moneda", planUsuario.getMoneda());

            ResultSet resultSet = statement.getResultSet();

            PlanUsuario planUsuarioRegistered = null;

            if (resultSet.first()) {
                planUsuarioRegistered = new PlanUsuario();
                planUsuarioRegistered.setUsuario(new Usuario(resultSet.getLong("id_usuario")));
                planUsuarioRegistered.setPlan(new Plan(resultSet.getLong("id_plan")));
                planUsuarioRegistered.setActive(resultSet.getBoolean("is_active"));
                planUsuarioRegistered.setMonto(resultSet.getDouble("monto"));
                planUsuarioRegistered.setMoneda(resultSet.getString("moneda"));
            }

            resultSet.close();

            if (planUsuarioRegistered != null) {
                return planUsuarioRegistered;
            } else {
                throw new NotCreatedException("No se creó el plan de usuario");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se creó el plan de usuario");
        }
    }
}
