package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.ISkillUsuarioRepository;
import com.main.skillexchangeapi.domain.entities.Categoria;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.entities.SubCategoria;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
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
public class SkillUsuarioRepository implements ISkillUsuarioRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public List<SkillUsuario> registrarMultiple(List<SkillUsuario> skillsUsuario) throws DatabaseNotWorkingException, NotCreatedException {
        List<SkillUsuario> skillsUsuarioRegistered = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_skill_usuario(?, ?, ?)}");) {
            skillsUsuario.forEach(skillUsuario -> {
                try {
                    statement.setBytes("p_id_usuario", UuidManager.UuidToBytes(skillUsuario.getUsuario().getId()));
                    statement.setBytes("p_id_skill", UuidManager.UuidToBytes(skillUsuario.getSkill().getId()));
                    statement.setInt("p_nivel_conocimiento", skillUsuario.getNivelConocimiento());

                    ResultSet resultSet = statement.getResultSet();

                    SkillUsuario skillUsuarioRegistered = null;

                    if (resultSet.first()) {
                        skillUsuarioRegistered = SkillUsuario.builder()
                                .skill(skillUsuario.getSkill())
                                .usuario(skillUsuario.getUsuario())
                                .nivelConocimiento(resultSet.getInt("NIVEL_CONOCIMIENTO"))
                                .build();
                    }

                    resultSet.close();

                    if (skillUsuarioRegistered != null) {
                        skillsUsuarioRegistered.add(skillUsuarioRegistered);
                    } else {
                        throw new NotCreatedException("No se creó el skill");
                    }
                } catch (SQLException | NotCreatedException e) {
                    skillsUsuarioRegistered.clear();
                }
            });

            if (!skillsUsuarioRegistered.isEmpty()) {
                return skillsUsuarioRegistered;
            } else {
                throw new NotCreatedException("No se crearon los skills");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se crearon los skills");
        }
    }

    @Override
    public List<SkillUsuario> obtenerByIdUsuario(UUID idUsuario) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL obtener_skills_from_usuario(?)}");) {
            statement.setBytes("p_id_usuario", UuidManager.UuidToBytes(idUsuario));

            ResultSet resultSet = statement.executeQuery();
            List<SkillUsuario> skillsUsuario = new ArrayList<>();

            while (resultSet.next()) {
                Categoria categoria = Categoria.builder()
                        .nombre(resultSet.getString("NOMBRE_CATEGORIA"))
                        .build();

                SubCategoria subCategoria=SubCategoria.builder()
                        .nombre(resultSet.getString("NOMBRE_SUB_CATEGORIA"))
                        .categoria(categoria)
                        .build();

                Skill skill = Skill.builder()
                        .id(UUID.fromString(resultSet.getString("ID")))
                        .descripcion(resultSet.getString("DESCRIPCION"))
                        .subCategoria(subCategoria)
                        .build();

                SkillUsuario skillUsuario = SkillUsuario.builder()
                        .nivelConocimiento(resultSet.getInt("NIVEL_CONOCIMIENTO"))
                        .skill(skill)
                        .build();

                skillsUsuario.add(skillUsuario);
            }

            resultSet.close();

            if (!skillsUsuario.isEmpty()) {
                return skillsUsuario;
            } else {
                throw new ResourceNotFoundException("El usuario no tiene skills");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se pudieron buscar los skills");
        }
    }
}
