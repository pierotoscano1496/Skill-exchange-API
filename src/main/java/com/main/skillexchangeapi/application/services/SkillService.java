package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.requests.CreateSkillRequest;
import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.domain.abstractions.repositories.ICategoriaRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.ISkillRepository;
import com.main.skillexchangeapi.domain.abstractions.services.ISkillService;
import com.main.skillexchangeapi.domain.entities.Categoria;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillService implements ISkillService {
    @Autowired
    private ISkillRepository repository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Override
    public SkillResponse registrar(CreateSkillRequest request) throws DatabaseNotWorkingException, NotCreatedException {
        Categoria categoria = Categoria.builder()
                .id(request.getIdCategoria())
                .build();

        Skill skill = Skill.builder()
                .nombre(request.getNombre())
                .categoria(categoria)
                .build();

        Skill skillRegistered = repository.registrar(skill);

        SkillResponse response = SkillResponse.builder()
                .id(skillRegistered.getId())
                .nombre(skillRegistered.getNombre())
                .idCcategoria(skillRegistered.getId())
                .build();

        return response;
    }
}
