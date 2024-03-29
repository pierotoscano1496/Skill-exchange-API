package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IServicioRepository;
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
import java.util.UUID;

@Repository
public class ServicioRepository implements IServicioRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public Servicio registrar(Servicio servicio) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_servicio(?, ?, ?, ?, ?, ?)}")) {
            statement.setString("p_titulo", servicio.getTitulo());
            statement.setString("p_descripcion", servicio.getDescripcion());
            statement.setDouble("p_precio", servicio.getPrecio());
            statement.setObject("p_id", UuidManager.UuidToBytes(UUID.randomUUID()));
            statement.setObject("p_id_usuario", UuidManager.UuidToBytes(servicio.getSkillUsuario().getUsuario().getId()));
            statement.setObject("p_id_skill", UuidManager.UuidToBytes(servicio.getSkillUsuario().getSkill().getId()));

            try (ResultSet resultSet = statement.executeQuery()) {
                Servicio servicioRegistered = null;

                while (resultSet.next()) {
                    servicioRegistered = Servicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .titulo(resultSet.getString("TITULO"))
                            .descripcion(resultSet.getString("DESCRIPCION"))
                            .precio(resultSet.getDouble("PRECIO"))
                            .skillUsuario(servicio.getSkillUsuario())
                            .build();

                    break;
                }

                if (servicioRegistered != null) {
                    return servicioRegistered;
                } else {
                    throw new NotCreatedException("No se creó el servicio");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se creó el servicio");
        }
    }
}
