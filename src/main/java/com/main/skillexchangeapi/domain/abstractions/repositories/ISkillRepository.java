package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.entities.detail.SkillInfo;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public interface ISkillRepository {
    List<Skill> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException;

    List<SkillInfo> obtenerInfo() throws DatabaseNotWorkingException, ResourceNotFoundException;

    List<Skill> obtenerBySubCategoria(UUID idSubcategoria)
            throws DatabaseNotWorkingException, ResourceNotFoundException;

    Skill registrar(Skill skill) throws DatabaseNotWorkingException, NotCreatedException;
}
