package com.main.skillexchangeapi.domain.abstractions.security.services;

import com.main.skillexchangeapi.app.security.entities.UsuarioCredenciales;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.domain.exceptions.UnsuccessLoginException;

public interface IAuthService {
    String getTokenLogin(UsuarioCredenciales credenciales) throws DatabaseNotWorkingException, ResourceNotFoundException, UnsuccessLoginException;
}
