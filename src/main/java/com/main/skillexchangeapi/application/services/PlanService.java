package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.responses.PlanResponse;
import com.main.skillexchangeapi.domain.abstractions.repositories.IPlanRepository;
import com.main.skillexchangeapi.domain.abstractions.services.IPlanService;
import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService implements IPlanService {
    @Autowired
    private IPlanRepository repository;

    @Override
    public List<PlanResponse> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException {
        List<Plan> planes = repository.obtener();

        return planes.stream().map(p -> PlanResponse.builder()
                .id(p.getId())
                .codigo(p.getCodigo())
                .tipo(p.getTipo())
                .isFree(p.isFree())
                .build()).collect(Collectors.toList());
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
