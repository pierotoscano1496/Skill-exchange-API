package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IMatchRepository;
import com.main.skillexchangeapi.domain.entities.Match;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Repository
public class MatchRepository implements IMatchRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public Match registrar(Match match) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_match (?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
            statement.setDate("p_fecha", java.sql.Date.valueOf(match.getFecha()));
            statement.setDate("p_fecha_inicio", java.sql.Date.valueOf(match.getFechaInicio()));
            statement.setDate("p_fecha_cierre", java.sql.Date.valueOf(match.getFechaCierre()));
            statement.setInt("p_estado", match.getEstado());
            statement.setDouble("p_costo", match.getCosto());
            statement.setObject("p_id_servicio", UuidManager.UuidToBytes(match.getServicio().getId()));
            statement.setObject("p_id_cliente", UuidManager.UuidToBytes(match.getCliente().getId()));

            ResultSet resultSet = statement.getResultSet();

            Match matchRegistered = null;

            if (resultSet.first()) {
                matchRegistered = Match.builder()
                        .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                        .fecha(resultSet.getDate("FECHA").toLocalDate())
                        .fechaInicio(resultSet.getDate("FECHA_INICIO").toLocalDate())
                        .fechaCierre(resultSet.getDate("FECHA_CIERRE").toLocalDate())
                        .estado(resultSet.getInt("ESTADO"))
                        .puntuacion(resultSet.getInt("PUNTUACION"))
                        .costo(resultSet.getDouble("COSTO"))
                        .servicio(match.getServicio())
                        .cliente(match.getCliente())
                        .build();
            }

            resultSet.close();

            if (matchRegistered != null) {
                return matchRegistered;
            } else {
                throw new NotCreatedException("No se creó el match");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se creó el match");
        }
    }
}
