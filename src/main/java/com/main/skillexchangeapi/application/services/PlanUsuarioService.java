package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.domain.abstractions.repositories.IPlanUsuarioRepository;
import com.main.skillexchangeapi.domain.abstractions.services.IPlanUsuarioService;
import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanUsuarioService implements IPlanUsuarioService {
    @Autowired
    private IPlanUsuarioRepository repository;

    @Override
    public PlanUsuario registrar(PlanUsuario planUsuario) throws DatabaseNotWorkingException, NotCreatedException {
        return repository.registrar(planUsuario);
    }
}
