package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.TestingModel;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.UUID;

public interface ITestingRepository {
    TestingModel registrar(TestingModel testingModel) throws DatabaseNotWorkingException, NotCreatedException;

    TestingModel obtener(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException;
}
