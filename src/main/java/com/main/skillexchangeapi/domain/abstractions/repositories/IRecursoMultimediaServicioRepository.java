package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.RecursoMultimediaServicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface IRecursoMultimediaServicioRepository {
    List<RecursoMultimediaServicio> obtenerByServicio(UUID idServicio) throws DatabaseNotWorkingException, ResourceNotFoundException;

    List<RecursoMultimediaServicio> registrarMultiple(List<RecursoMultimediaServicio> recursosMultimediaServicio) throws DatabaseNotWorkingException, NotCreatedException;
}
