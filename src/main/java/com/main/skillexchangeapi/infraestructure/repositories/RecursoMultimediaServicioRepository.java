package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.constants.RecursoMultimediaContants.Medio;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IRecursoMultimediaServicioRepository;
import com.main.skillexchangeapi.domain.entities.RecursoMultimediaServicio;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotDeletedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<RecursoMultimediaServicio> obtenerByServicio(UUID idServicio)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("CALL obtener_recursos_multimedia_servicio_by_servicio(?)")) {
            statement.setBytes("p_id_servicio", UuidManager.UuidToBytes(idServicio));

            try (ResultSet resultSet = statement.executeQuery()) {
                List<RecursoMultimediaServicio> recursosMultimediaServicio = new ArrayList<>();

                while (resultSet.next()) {
                    recursosMultimediaServicio.add(RecursoMultimediaServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .servicio(Servicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                    .build())
                            .medio(Medio.valueOf(resultSet.getString("MEDIO")))
                            .url(resultSet.getString("URL"))
                            .build());
                }

                if (!recursosMultimediaServicio.isEmpty()) {
                    return recursosMultimediaServicio;
                } else {
                    throw new ResourceNotFoundException("No se encontraron recursos");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error durante la búsqueda de servicios");
        }
    }

    @Override
    public List<RecursoMultimediaServicio> registrarMultiple(List<RecursoMultimediaServicio> recursosMultimediaServicio)
            throws DatabaseNotWorkingException, NotCreatedException {
        List<RecursoMultimediaServicio> recursosMultimediaServicioRegistered = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("{CALL registrar_recurso_multimedia_servicio(?, ?, ?, ?)}")) {

            for (RecursoMultimediaServicio recursoMultimediaServicio : recursosMultimediaServicio) {
                try {
                    byte[] idRecursoMultimediaToBytes = UuidManager.randomUuidToBytes();
                    statement.setObject("p_id", idRecursoMultimediaToBytes);
                    statement.setString("p_url", recursoMultimediaServicio.getUrl());
                    statement.setString("p_medio", recursoMultimediaServicio.getMedio().toString());
                    statement.setObject("p_id_servicio",
                            UuidManager.UuidToBytes(recursoMultimediaServicio.getServicio().getId()));

                    try (ResultSet resultSet = statement.executeQuery()) {
                        RecursoMultimediaServicio recursoMultimediaServicioRegistered = null;

                        while (resultSet.next()) {
                            recursoMultimediaServicioRegistered = RecursoMultimediaServicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                                    .url(resultSet.getString("URL"))
                                    .medio(Medio.valueOf(resultSet.getString("MEDIO")))
                                    .servicio(Servicio.builder()
                                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                            .build())
                                    .build();

                            break;
                        }

                        if (recursoMultimediaServicioRegistered != null) {
                            recursosMultimediaServicioRegistered.add(recursoMultimediaServicioRegistered);
                        }
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

    @Override
    public List<UUID> eliminarMultiple(List<UUID> idsRecursosMultimediaServicio)
            throws DatabaseNotWorkingException, ResourceNotFoundException, NotDeletedException {
        List<UUID> deletedIds = new ArrayList<>();
        for (UUID idRecursoMultimediaServicio : idsRecursosMultimediaServicio) {
            if (eliminar(idRecursoMultimediaServicio)) {
                deletedIds.add(idRecursoMultimediaServicio);
            }
        }
        if (deletedIds.size() == idsRecursosMultimediaServicio.size()) {
            return deletedIds;
        } else {
            throw new NotDeletedException("No se eliminaron todos los recursos multimedia");
        }
    }

    public boolean eliminar(UUID id)
            throws DatabaseNotWorkingException, ResourceNotFoundException, NotDeletedException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("{CALL eliminar_recurso_multimedia_servicio(?)}");) {
            if (id == null) {
                throw new NotDeletedException("El ID del recurso multimedia no puede ser nulo");
            }
            statement.setBytes("p_id", UuidManager.UuidToBytes(id));
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            } else {
                throw new NotDeletedException("No se eliminó el recurso multimedia");
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar recurso {}", id, e);
            throw new DatabaseNotWorkingException("No se eliminó el recurso multimedia");
        }
    }
}
