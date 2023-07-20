package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.ISkillRepository;
import com.main.skillexchangeapi.domain.entities.Categoria;
import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
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
import java.util.UUID;

@Repository
public class SkillRepository implements ISkillRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public Skill registrar(Skill skill) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_skill(?, ?)}");) {
            statement.setString("p_nombre", skill.getNombre());
            statement.setBytes("p_id_categoria", UuidManager.UuidToBytes(skill.getCategoria().getId()));

            ResultSet resultSet = statement.getResultSet();

            Skill skillRegistered = null;

            if (resultSet.first()) {
                skillRegistered = Skill.builder()
                        .id(UUID.fromString(resultSet.getString("id")))
                        .nombre(resultSet.getString("nombre"))
                        .build();

                Categoria categoria = Categoria.builder()
                        .id(UUID.fromString(resultSet.getString("id_categoria")))
                        .build();

                skillRegistered.setCategoria(categoria);
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
