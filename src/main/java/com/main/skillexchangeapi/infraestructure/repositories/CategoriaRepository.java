package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.domain.abstractions.repositories.ICategoriaRepository;
import com.main.skillexchangeapi.domain.entities.Categoria;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class CategoriaRepository implements ICategoriaRepository {
    @Override
    public Categoria obtenerById(UUID id) {
        return null;
    }
}
