package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;

public interface IPlanUsuarioRepository {
    public PlanUsuario registrar(PlanUsuario planUsuario) throws DatabaseNotWorkingException, NotCreatedException;
}
