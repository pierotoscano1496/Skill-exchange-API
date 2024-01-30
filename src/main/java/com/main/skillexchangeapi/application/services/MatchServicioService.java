package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.domain.abstractions.repositories.IMatchServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.services.IMatchServicioService;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MatchServicioService implements IMatchServicioService {
    @Autowired
    private IMatchServicioRepository repository;

    @Override
    public MatchServicio registrar(MatchServicio matchServicio) throws DatabaseNotWorkingException, NotCreatedException {
        return repository.registrar(matchServicio);
    }

    @Override
    public MatchServicio actualizarEstado(UUID id, String estado) throws DatabaseNotWorkingException, NotUpdatedException {
        return repository.actualizarEstado(id, estado);
    }
}
