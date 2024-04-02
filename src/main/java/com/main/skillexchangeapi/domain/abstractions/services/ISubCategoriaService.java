package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.responses.usuario.SubCategoriaResponse;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ISubCategoriaService {
    List<SubCategoriaResponse> obtenerByCategoria(UUID idCategoria) throws DatabaseNotWorkingException, ResourceNotFoundException;
}
