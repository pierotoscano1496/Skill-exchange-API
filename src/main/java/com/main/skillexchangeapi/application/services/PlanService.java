package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.domain.abstractions.repositories.IPlanRepository;
import com.main.skillexchangeapi.domain.abstractions.services.IPlanService;
import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PlanService implements IPlanService {
    @Autowired
    private IPlanRepository repository;

    @Override
    public ArrayList<Plan> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.obtener();
    }

    @Override
    public Plan obtenerPlanGratuito() throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.obtenerByCodigo("free");
    }

    @Override
    public Plan obtenerByCodigo(String codigo) throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.obtenerByCodigo(codigo);
    }

}
