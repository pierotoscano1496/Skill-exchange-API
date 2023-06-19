package com.main.skillexchangeapi.infraestructure.repositories;

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

@Repository
public class SkillRepository implements ISkillRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public Skill registrar(Skill skill) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_skill(?, ?)}");) {
            statement.setString("p_nombre", skill.getNombre());
            statement.setLong("p_id_categoria", skill.getCategoria().getId());

            ResultSet resultSet = statement.getResultSet();

            Skill skillRegistered = null;

            if (resultSet.first()) {
                skillRegistered = new Skill(resultSet.getLong("id"));
                skillRegistered.setNombre(resultSet.getString("nombre"));

                Categoria categoria = new Categoria(resultSet.getLong("id_categoria"));
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
