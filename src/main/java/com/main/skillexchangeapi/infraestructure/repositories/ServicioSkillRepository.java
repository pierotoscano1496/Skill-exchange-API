package com.main.skillexchangeapi.infraestructure.repositories;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.main.skillexchangeapi.app.constants.ServicioConstants.Modalidad;
import com.main.skillexchangeapi.app.constants.ServicioConstants.TipoPrecio;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IServicioSkillRepository;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.entities.detail.ServicioSkill;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;

@Repository
public class ServicioSkillRepository implements IServicioSkillRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<ServicioSkill> obtenerDetailsByProveedor(UUID idProveedor)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        List<ServicioSkill> servicioSkills = new ArrayList<>();
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("{CALL obtener_servicio_skill_details_by_proveedor(?)}")) {
            statement.setBytes("p_id_proveedor", UuidManager.UuidToBytes(idProveedor));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Servicio servicio = Servicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                            .descripcion(resultSet.getString("DESCRIPCION_SERVICIO"))
                            .aceptaTerminos(resultSet.getBoolean("ACEPTA_TERMINOS_SERVICIO"))
                            .modalidad(Modalidad.valueOf(resultSet.getString("MODALIDAD_SERVICIO")))
                            .precio(resultSet.getDouble("PRECIO_SERVICIO"))
                            .precioMaximo(resultSet.getDouble("PRECIO_MAXIMO_SERVICIO"))
                            .precioMinimo(resultSet.getDouble("PRECIO_MINIMO_SERVICIO"))
                            .tipoPrecio(TipoPrecio.valueOf(resultSet.getString("TIPO_PRECIO_SERVICIO")))
                            .titulo(resultSet.getString("TITULO_SERVICIO"))
                            .ubicacion(resultSet.getString("UBICACION_SERVICIO"))
                            .build();

                    Skill skill = Skill.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SKILL")))
                            .descripcion(resultSet.getString("DESCRIPCION_SKILL"))
                            .build();

                    ServicioSkill servicioSkill = ServicioSkill.builder()
                            .servicio(servicio)
                            .skill(skill)
                            .build();
                    servicioSkills.add(servicioSkill);
                }
            }

            if (servicioSkills.isEmpty()) {
                throw new ResourceNotFoundException("No existen skills para el proveedor con ID: " + idProveedor);
            }
            return servicioSkills;
        } catch (SQLException e) {
            logger.error("Error al obtener los skills del proveedor con ID: {}", idProveedor, e);
            throw new DatabaseNotWorkingException("No se obtuvieron los skills del proveedor");
        }
    }

    @Override
    public List<ServicioSkill> registrarMultiple(List<ServicioSkill> servicioSkills)
            throws DatabaseNotWorkingException, NotCreatedException {
        List<ServicioSkill> servicioSkillsRegistered = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection.prepareCall("{CALL registrar_servicio_skill(?, ?)}");) {
            for (ServicioSkill servicioSkill : servicioSkills) {
                try {
                    statement.setBytes("p_id_servicio", UuidManager.UuidToBytes(servicioSkill.getServicio().getId()));
                    statement.setBytes("p_id_skill", UuidManager.UuidToBytes(servicioSkill.getSkill().getId()));

                    try (ResultSet resultSet = statement.executeQuery()) {
                        ServicioSkill servicioSkillRegistered = null;

                        while (resultSet.next()) {
                            servicioSkillRegistered = ServicioSkill.builder()
                                    .servicio(servicioSkill.getServicio())
                                    .skill(servicioSkill.getSkill())
                                    .build();

                            break;
                        }

                        if (servicioSkillRegistered != null) {
                            servicioSkillsRegistered.add(servicioSkillRegistered);
                        }
                    }
                } catch (Exception e) {
                    servicioSkillsRegistered.clear();
                }
            }

            if (servicioSkillsRegistered.size() == servicioSkills.size()) {
                return servicioSkillsRegistered;
            } else {
                throw new NotCreatedException("No se registraron los skills del servicio");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se registraron los skills del servicio");
        }
    }

}
