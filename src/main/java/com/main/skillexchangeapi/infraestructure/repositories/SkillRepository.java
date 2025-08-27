package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.ISkillRepository;
import com.main.skillexchangeapi.domain.entities.*;
import com.main.skillexchangeapi.domain.entities.detail.SkillInfo;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
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
public class SkillRepository implements ISkillRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<Skill> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection.prepareCall("CALL obtener_skills()");
                ResultSet resultSet = statement.executeQuery()) {

            List<Skill> skills = new ArrayList<>();

            while (resultSet.next()) {
                skills.add(Skill.builder()
                        .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                        .descripcion(resultSet.getString("DESCRIPCION"))
                        .subCategoria(SubCategoria.builder()
                                .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SUB_CATEGORIA")))
                                .build())
                        .build());
            }

            if (!skills.isEmpty()) {
                return skills;
            } else {
                throw new ResourceNotFoundException("No se encontraron habilidades para la subcategoría indicada");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error de búsqueda de habilidades");
        }
    }

    @Override
    public Skill obtenerById(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection.prepareCall("{CALL obtener_skill(?)}")) {
            statement.setBytes("p_id", UuidManager.UuidToBytes(id));

            try (ResultSet resultSet = statement.executeQuery()) {
                Skill skill = null;

                while (resultSet.next()) {
                    skill = Skill.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .descripcion(resultSet.getString("DESCRIPCION"))
                            .subCategoria(SubCategoria.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SUB_CATEGORIA")))
                                    .build())
                            .build();

                    break;
                }
                if (skill != null) {
                    return skill;
                } else {
                    throw new ResourceNotFoundException("No se encontró la habilidad con el ID indicado");
                }
            }
        } catch (SQLException e) {
            logger.error("Error al obtener skill por ID", e);
            throw new DatabaseNotWorkingException("Error de búsqueda de habilidad por ID");
        }
    }

    @Override
    public List<SkillInfo> obtenerInfo() throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection.prepareCall("CALL obtener_skills_info()");
                ResultSet resultSet = statement.executeQuery()) {

            List<SkillInfo> skills = new ArrayList<>();

            while (resultSet.next()) {
                skills.add(SkillInfo.builder()
                        .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                        .descripcion(resultSet.getString("DESCRIPCION"))
                        .nombreCategoria(resultSet.getString("NOMBRE_CATEGORIA"))
                        .nombreSubCategoria(resultSet.getString("NOMBRE_SUB_CATEGORIA"))
                        .build());
            }

            if (!skills.isEmpty()) {
                return skills;
            } else {
                throw new ResourceNotFoundException("No se encontraron habilidades para la subcategoría indicada");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error de búsqueda de habilidades");
        }
    }

    @Override
    public List<Skill> obtenerBySubCategoria(UUID idSubcategoria)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection.prepareCall("CALL obtener_skills_by_sub_categoria(?)")) {
            statement.setBytes("p_id_sub_categoria", UuidManager.UuidToBytes(idSubcategoria));

            ResultSet resultSet = statement.executeQuery();

            List<Skill> skills = new ArrayList<>();

            while (resultSet.next()) {
                skills.add(Skill.builder()
                        .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                        .descripcion(resultSet.getString("DESCRIPCION"))
                        .subCategoria(SubCategoria.builder()
                                .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SUB_CATEGORIA")))
                                .build())
                        .build());
            }

            if (!skills.isEmpty()) {
                return skills;
            } else {
                throw new ResourceNotFoundException("No se encontraron habilidades para la subcategoría indicada");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error de búsqueda de habilidades");
        }
    }

    @Override
    public Skill registrar(Skill skill) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection.prepareCall("{CALL registrar_skill(?, ?)}");) {
            statement.setString("p_descripcion", skill.getDescripcion());
            statement.setBytes("p_id_sub_categoria", UuidManager.UuidToBytes(skill.getSubCategoria().getId()));

            ResultSet resultSet = statement.getResultSet();

            Skill skillRegistered = null;

            if (resultSet.first()) {
                skillRegistered = Skill.builder()
                        .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                        .descripcion(resultSet.getString("DESCRIPCION"))
                        .build();

                SubCategoria subCategoria = SubCategoria.builder()
                        .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SUB_CATEGORIA")))
                        .build();

                skillRegistered.setSubCategoria(subCategoria);
            }

            resultSet.close();

            if (skillRegistered != null) {
                return skillRegistered;
            } else {
                throw new NotCreatedException("No se creó el skill");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se creó el skill");
        }
    }
}
