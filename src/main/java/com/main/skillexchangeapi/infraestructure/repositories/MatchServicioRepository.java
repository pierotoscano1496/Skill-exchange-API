package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IMatchServicioRepository;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Repository
public class MatchServicioRepository implements IMatchServicioRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public MatchServicio registrar(MatchServicio match) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_match (?, ?, ?, ?, ?, ?, ?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(match.getId()));
            statement.setDate("p_fecha", java.sql.Date.valueOf(match.getFecha()));
            statement.setDate("p_fecha_inicio", java.sql.Date.valueOf(match.getFechaInicio()));
            statement.setDate("p_fecha_cierre", java.sql.Date.valueOf(match.getFechaCierre()));
            statement.setString("p_estado", match.getEstado());
            statement.setDouble("p_costo", match.getCosto());
            statement.setObject("p_id_servicio", UuidManager.UuidToBytes(match.getServicio().getId()));
            statement.setObject("p_id_cliente", UuidManager.UuidToBytes(match.getCliente().getId()));

            try (ResultSet resultSet = statement.getResultSet()) {
                MatchServicio matchRegistered = null;

                if (resultSet.first()) {
                    matchRegistered = MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getDate("FECHA").toLocalDate())
                            .fechaInicio(resultSet.getDate("FECHA_INICIO").toLocalDate())
                            .fechaCierre(resultSet.getDate("FECHA_CIERRE").toLocalDate())
                            .estado(resultSet.getString("ESTADO"))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
                            .servicio(match.getServicio())
                            .cliente(match.getCliente())
                            .build();
                }

                if (matchRegistered != null) {
                    return matchRegistered;
                } else {
                    throw new NotCreatedException("No se creó el match");
                }
            } catch (SQLException e) {
                throw new DatabaseNotWorkingException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se creó el match");
        }
    }

    @Override
    public MatchServicio actualizarEstado(UUID id, String estado) throws DatabaseNotWorkingException, NotUpdatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL actualizar_match_servicio_estado (?, ?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(id));
            statement.setString("p_estado", estado);

            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.first()) {
                    return MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getDate("FECHA").toLocalDate())
                            .fechaInicio(resultSet.getDate("FECHA_INICIO").toLocalDate())
                            .fechaCierre(resultSet.getDate("FECHA_CIERRE").toLocalDate())
                            .estado(resultSet.getString("ESTADO"))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
                            .servicio(Servicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                    .build())
                            .cliente(Usuario.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_CLIENTE")))
                                    .build())
                            .build();
                } else {
                    throw new NotUpdatedException("No se actualizó el estado del match");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se actualizó el estado del match");
        }
    }
}
