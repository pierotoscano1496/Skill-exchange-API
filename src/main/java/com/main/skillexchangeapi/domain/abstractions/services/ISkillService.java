package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.requests.CreateSkillRequest;
import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;

public interface ISkillService {
    SkillResponse registrar(CreateSkillRequest request) throws DatabaseNotWorkingException, NotCreatedException;
}
