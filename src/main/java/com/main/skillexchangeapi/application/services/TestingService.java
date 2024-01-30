package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.domain.abstractions.repositories.ITestingRepository;
import com.main.skillexchangeapi.domain.abstractions.services.ITestingService;
import com.main.skillexchangeapi.domain.entities.TestingModel;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TestingService implements ITestingService {
    @Autowired
    private ITestingRepository repository;

    @Override
    public TestingModel registrar(TestingModel testingModel) throws DatabaseNotWorkingException, NotCreatedException {
        return repository.registrar(testingModel);
    }

    @Override
    public TestingModel obtener(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.obtener(id);
    }
}
