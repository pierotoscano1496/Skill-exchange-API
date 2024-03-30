package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.responses.usuario.CategoriaResponse;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.List;

public interface ICategoriaService {
    List<CategoriaResponse> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException;
}
