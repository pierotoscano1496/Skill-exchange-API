package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.app.responses.usuario.CategoriaResponse;
import com.main.skillexchangeapi.app.responses.usuario.SubCategoriaResponse;
import com.main.skillexchangeapi.domain.abstractions.repositories.ICategoriaRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.ISkillRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.ISubCategoriaRepository;
import com.main.skillexchangeapi.domain.abstractions.services.ICategoriaService;
import com.main.skillexchangeapi.domain.entities.Categoria;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.entities.SubCategoria;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService implements ICategoriaService {
    @Autowired
    private ICategoriaRepository repository;

    @Autowired
    private ISubCategoriaRepository subCategoriaRepository;

    @Autowired
    private ISkillRepository skillRepository;

    @Override
    public List<CategoriaResponse> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.obtener().stream().map(c -> CategoriaResponse.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoriaResponse> obtenerDetails() throws DatabaseNotWorkingException, ResourceNotFoundException {
        List<Categoria> categorias = repository.obtener();
        List<SubCategoria> subCategorias = subCategoriaRepository.obtener();
        List<Skill> skills = skillRepository.obtener();

        return categorias.stream().map(categoria -> CategoriaResponse.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .subCategorias(subCategorias.stream()
                        .filter(subCategoria -> subCategoria.getCategoria().getId().equals(categoria.getId()))
                        .map(subCategoria -> SubCategoriaResponse.builder()
                                .id(subCategoria.getId())
                                .nombre(subCategoria.getNombre())
                                .skills(skills.stream()
                                        .filter(skill -> skill.getSubCategoria().getId()
                                                .equals(subCategoria.getId()))
                                        .map(skill -> SkillResponse.builder()
                                                .id(skill.getId())
                                                .descripcion(skill.getDescripcion())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }
}
