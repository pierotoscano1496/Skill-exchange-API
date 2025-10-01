package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.responses.usuario.SubCategoriaResponse;
import com.main.skillexchangeapi.domain.abstractions.repositories.ISubCategoriaRepository;
import com.main.skillexchangeapi.domain.abstractions.services.ISubCategoriaService;
import com.main.skillexchangeapi.domain.entities.SubCategoria;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubCategoriaService implements ISubCategoriaService {
    @Autowired
    private ISubCategoriaRepository repository;

    @Override
    public List<SubCategoriaResponse> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException {
        List<SubCategoria> subCategorias = repository.obtener();
        return subCategorias.stream().map(s -> SubCategoriaResponse.builder()
                .id(s.getId())
                .nombre(s.getNombre())
                .idCategoria(s.getCategoria().getId())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<SubCategoriaResponse> obtenerByCategoria(UUID idCategoria) throws DatabaseNotWorkingException, ResourceNotFoundException {
        List<SubCategoria> subCategorias = repository.obtenerByCategoria(idCategoria);
        return subCategorias.stream().map(s -> SubCategoriaResponse.builder()
                .id(s.getId())
                .nombre(s.getNombre())
                .idCategoria(s.getCategoria().getId())
                .build()).collect(Collectors.toList());
    }
}
