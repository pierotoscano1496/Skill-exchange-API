package com.main.skillexchangeapi.infraestructure.repositories;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IServicioDisponibilidadRepository;
import com.main.skillexchangeapi.domain.entities.detail.ServicioDisponibilidad;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;

@Repository
public class ServicioDisponibilidadRepository implements IServicioDisponibilidadRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public List<ServicioDisponibilidad> registrarMultiple(List<ServicioDisponibilidad> servicioDisponibilidades) throws DatabaseNotWorkingException, NotCreatedException {
        List<ServicioDisponibilidad> servicioDisponibilidadesRegistered = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_servicio_disponibilidad(?, ?, ?, ?, ?)}");) {
            for (ServicioDisponibilidad servicioDisponibilidad : servicioDisponibilidades) {
                try {
                    statement.setBytes("p_id", UuidManager.randomUuidToBytes());
                    statement.setBytes("p_id_servicio", UuidManager.UuidToBytes(servicioDisponibilidad.getServicio().getId()));
                    statement.setString("p_dia", servicioDisponibilidad.getDia());
                    statement.setTime("p_hora_inicio", java.sql.Time.valueOf(servicioDisponibilidad.getHoraInicio()));
                    statement.setTime("p_hora_fin", java.sql.Time.valueOf(servicioDisponibilidad.getHoraFin()));

                    try (ResultSet resultSet = statement.executeQuery()) {
                        ServicioDisponibilidad servicioDisponibilidadRegistered = null;

                        while (resultSet.next()) {
                            servicioDisponibilidadRegistered = ServicioDisponibilidad.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                                    .servicio(servicioDisponibilidad.getServicio())
                                    .dia(resultSet.getString("DIA"))
                                    .horaInicio(resultSet.getTime("HORA_INICIO").toLocalTime())
                                    .horaFin(resultSet.getTime("HORA_FIN").toLocalTime())
                                    .build();

                            break;
                        }

                        if (servicioDisponibilidadRegistered != null) {
                            servicioDisponibilidadesRegistered.add(servicioDisponibilidadRegistered);
                        }
                    }
                } catch (SQLException e) {
                    servicioDisponibilidadesRegistered.clear();
                }
            }

            if (servicioDisponibilidadesRegistered.size() == servicioDisponibilidades.size()) {
                return servicioDisponibilidadesRegistered;
            } else {
                throw new NotCreatedException("No se pudieron registrar todas las disponibilidades del servicio");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se pudieron registrar todas las disponibilidades del servicio");
        }
    }

}
