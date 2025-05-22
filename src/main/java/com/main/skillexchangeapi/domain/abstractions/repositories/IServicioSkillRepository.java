package com.main.skillexchangeapi.domain.abstractions.repositories;

import java.util.List;

import com.main.skillexchangeapi.domain.entities.detail.ServicioSkill;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;

public interface IServicioSkillRepository {
    List<ServicioSkill> registrarMultiple(List<ServicioSkill> servicioSkills) throws DatabaseNotWorkingException, NotCreatedException;
}
