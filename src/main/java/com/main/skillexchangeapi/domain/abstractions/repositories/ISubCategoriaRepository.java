package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.SubCategoria;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ISubCategoriaRepository {
    List<SubCategoria> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException;
    List<SubCategoria> obtenerByCategoria(UUID idCategoria) throws DatabaseNotWorkingException, ResourceNotFoundException;
}
