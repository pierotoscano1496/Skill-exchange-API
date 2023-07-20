package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.Categoria;

import java.util.UUID;

public interface ICategoriaRepository {
    Categoria obtenerById(UUID id);
}
