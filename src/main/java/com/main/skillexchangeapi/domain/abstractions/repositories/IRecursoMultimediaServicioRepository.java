package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.RecursoMultimediaServicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;

import java.util.List;

public interface IRecursoMultimediaServicioRepository {
    List<RecursoMultimediaServicio> registrarMultiple(List<RecursoMultimediaServicio> recursosMultimediaServicio) throws DatabaseNotWorkingException, NotCreatedException;
}
