package com.main.skillexchangeapi.domain.abstractions.repositories;

import java.util.List;
import java.util.UUID;
import com.main.skillexchangeapi.domain.entities.detail.ServicioSkill;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

public interface IServicioSkillRepository {
        List<ServicioSkill> obtenerDetailsByProveedor(UUID idProveedor)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;

        List<ServicioSkill> registrarMultiple(List<ServicioSkill> servicioSkills)
                        throws DatabaseNotWorkingException, NotCreatedException;
}
