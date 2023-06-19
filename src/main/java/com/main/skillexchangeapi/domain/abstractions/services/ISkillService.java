package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;

import java.util.ArrayList;

public interface ISkillService {
    public Skill registrar(Skill planUsuario) throws DatabaseNotWorkingException, NotCreatedException;
}
