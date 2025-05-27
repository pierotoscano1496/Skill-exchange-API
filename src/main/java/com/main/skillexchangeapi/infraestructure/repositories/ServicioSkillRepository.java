package com.main.skillexchangeapi.infraestructure.repositories;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IServicioSkillRepository;
import com.main.skillexchangeapi.domain.entities.detail.ServicioSkill;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;

@Repository
public class ServicioSkillRepository implements IServicioSkillRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

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
