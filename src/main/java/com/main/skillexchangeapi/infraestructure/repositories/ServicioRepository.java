package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IServicioRepository;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.domain.entities.searchparameters.SearchServicioParams;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ServicioRepository implements IServicioRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public List<Servicio> searchByParams(SearchServicioParams params) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("CALL search_servicios(?, ?, ?, ?)")) {
            statement.setString("p_key_word", params.getKeyWord());
            statement.setBytes("p_id_skill", UuidManager.UuidToBytes(params.getIdSkill()));
            statement.setBytes("p_id_subcategoria", UuidManager.UuidToBytes(params.getIdSubcategoria()));
            statement.setBytes("p_id_categoria", UuidManager.UuidToBytes(params.getIdCategoria()));

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Servicio> servicios = new ArrayList<>();

                while (resultSet.next()) {
                    servicios.add(Servicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .descripcion(resultSet.getString("DESCRIPCION"))
                            .precio(resultSet.getDouble("PRECIO"))
                            .titulo(resultSet.getString("TITULO"))
                            .skillUsuario(SkillUsuario.builder()
                                    .skill(Skill.builder()
                                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SKILL")))
                                            .descripcion("DESCRIPCION_SKILL")
                                            .build())
                                    .usuario(Usuario.builder()
                                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_USUARIO")))
                                            .nombres(resultSet.getString("NOMBRES_USUARIO"))
                                            .apellidos(resultSet.getString("APELLIDOS_USUARIO"))
                                            .correo(resultSet.getString("CORREO_USUARIO"))
                                            .build())
                                    .build())
                            .build());
                }

                if (!servicios.isEmpty()) {
                    return servicios;
                } else {
                    throw new ResourceNotFoundException("No existen los servicios indicados");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error durante la búsqueda de servicios");
        }
    }

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
