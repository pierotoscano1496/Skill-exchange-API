package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.domain.abstractions.repositories.ISkillRepository;
import com.main.skillexchangeapi.domain.abstractions.services.ISkillService;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SkillService implements ISkillService {
    @Autowired
    private ISkillRepository repository;

    @Override
    public Skill registrar(Skill skill) throws DatabaseNotWorkingException, NotCreatedException {
        return repository.registrar(skill);
    }
}
