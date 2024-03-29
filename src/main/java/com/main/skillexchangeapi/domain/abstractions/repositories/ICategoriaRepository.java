package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.Categoria;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ICategoriaRepository {
    Categoria obtenerById(UUID id);

    List<Categoria> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException;
}
