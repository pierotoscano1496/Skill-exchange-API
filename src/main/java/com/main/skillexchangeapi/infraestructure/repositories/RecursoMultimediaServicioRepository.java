package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IRecursoMultimediaServicioRepository;
import com.main.skillexchangeapi.domain.entities.RecursoMultimediaServicio;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
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
public class RecursoMultimediaServicioRepository implements IRecursoMultimediaServicioRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public List<RecursoMultimediaServicio> registrarMultiple(List<RecursoMultimediaServicio> recursosMultimediaServicio) throws DatabaseNotWorkingException, NotCreatedException {
        List<RecursoMultimediaServicio> recursosMultimediaServicioRegistered = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_recurso_multimedia_servicio(?, ?, ?, ?)}")) {
            connection.setAutoCommit(false);

            for (RecursoMultimediaServicio recursoMultimediaServicio : recursosMultimediaServicio) {
                try {
                    statement.setObject("p_id", UuidManager.UuidToBytes(recursoMultimediaServicio.getId()));
                    statement.setString("p_url", recursoMultimediaServicio.getUrl());
                    statement.setString("p_medio", recursoMultimediaServicio.getMedio());
                    statement.setObject("p_id_servicio", UuidManager.UuidToBytes(recursoMultimediaServicio.getServicio().getId()));

                    try (ResultSet resultSet = statement.getResultSet()) {
                        RecursoMultimediaServicio recursoMultimediaServicioRegistered = null;

                        if (resultSet.first()) {
                            recursoMultimediaServicioRegistered = RecursoMultimediaServicio.builder()
                                    .id(UUID.fromString(resultSet.getString("ID")))
                                    .url(resultSet.getString("URL"))
                                    .medio(resultSet.getString("MEDIO"))
                                    .servicio(Servicio.builder()
                                            .id(UUID.fromString(resultSet.getString("ID_SERVICIO")))
                                            .build())
                                    .build();
                        }

                        if (recursoMultimediaServicioRegistered != null) {
                            recursosMultimediaServicioRegistered.add(recursoMultimediaServicioRegistered);
                        }
                    } catch (SQLException e) {
                        throw new DatabaseNotWorkingException(e.getMessage());
                    }
                } catch (SQLException e) {
                    throw new DatabaseNotWorkingException(e.getMessage());
                }
            }

            if (recursosMultimediaServicioRegistered.size() == recursosMultimediaServicio.size()) {
                return recursosMultimediaServicioRegistered;
            } else {
                throw new NotCreatedException("No se crearon los recursos multimedia");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se crearon los recursos multimedia");
        }
    }
}
