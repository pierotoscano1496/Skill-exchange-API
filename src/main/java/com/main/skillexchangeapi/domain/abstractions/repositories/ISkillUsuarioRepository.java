package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ISkillUsuarioRepository {
    List<SkillUsuario> registrarMultiple(List<SkillUsuario> skillsUsuario) throws DatabaseNotWorkingException, NotCreatedException;

    List<SkillUsuario> obtenerByIdUsuario(UUID idUsuario) throws DatabaseNotWorkingException, ResourceNotFoundException;
}
