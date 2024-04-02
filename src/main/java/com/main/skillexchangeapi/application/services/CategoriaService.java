package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.responses.usuario.CategoriaResponse;
import com.main.skillexchangeapi.domain.abstractions.repositories.ICategoriaRepository;
import com.main.skillexchangeapi.domain.abstractions.services.ICategoriaService;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService implements ICategoriaService {
    @Autowired
    private ICategoriaRepository repository;

    @Override
    public List<CategoriaResponse> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.obtener().stream().map(c -> CategoriaResponse.builder()
                        .id(c.getId())
                        .nombre(c.getNombre())
                        .build())
                .collect(Collectors.toList());
    }
}
