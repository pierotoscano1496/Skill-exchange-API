package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;

import java.util.UUID;

public interface IMatchServicioService {
    MatchServicio registrar(MatchServicio matchServicio) throws DatabaseNotWorkingException, NotCreatedException;

    MatchServicio actualizarEstado(UUID id, String estado) throws DatabaseNotWorkingException, NotUpdatedException;
}
