package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.requests.CreateSkillRequest;
import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface ISkillService {
    List<SkillResponse> obtenerBySubCategoria(UUID idSubcategoria) throws DatabaseNotWorkingException, ResourceNotFoundException;
    SkillResponse registrar(CreateSkillRequest request) throws DatabaseNotWorkingException, NotCreatedException;
}
