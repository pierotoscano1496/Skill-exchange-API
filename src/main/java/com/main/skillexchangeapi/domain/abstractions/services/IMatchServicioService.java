package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.requests.matchservicio.CreateMatchServicioBody;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioResponse;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;

import java.util.UUID;

public interface IMatchServicioService {
    MatchServicioResponse registrar(CreateMatchServicioBody requestBody) throws DatabaseNotWorkingException, NotCreatedException;

    MatchServicioResponse actualizarEstado(UUID id, String estado) throws DatabaseNotWorkingException, NotUpdatedException;

    MatchServicioResponse puntuarServicio(UUID id, int puntuacion) throws DatabaseNotWorkingException, NotUpdatedException;
}
