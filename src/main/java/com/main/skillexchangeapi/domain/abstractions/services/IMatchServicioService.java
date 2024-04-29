package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.requests.matchservicio.CreateMatchServicioBody;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioResponse;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface IMatchServicioService {
    List<MatchServicioResponse> obtenerFromPrestamistaByOptionalEstado(UUID idPrestamista, String estado) throws DatabaseNotWorkingException, ResourceNotFoundException;

    MatchServicioResponse registrar(CreateMatchServicioBody requestBody) throws DatabaseNotWorkingException, NotCreatedException;

    MatchServicioResponse actualizarEstado(UUID id, String estado) throws DatabaseNotWorkingException, NotUpdatedException;

    MatchServicioResponse puntuarServicio(UUID id, int puntuacion) throws DatabaseNotWorkingException, NotUpdatedException;
}
