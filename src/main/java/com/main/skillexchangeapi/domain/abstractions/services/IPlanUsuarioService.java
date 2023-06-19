package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;

public interface IPlanUsuarioService {
    public PlanUsuario registrar(PlanUsuario planUsuario) throws DatabaseNotWorkingException, NotCreatedException;
}
