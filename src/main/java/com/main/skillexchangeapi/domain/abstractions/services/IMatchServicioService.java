package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;
import com.main.skillexchangeapi.app.requests.matchservicio.CreateMatchServicioBody;
import com.main.skillexchangeapi.app.requests.matchservicio.PuntajeServicioRequest;
import com.main.skillexchangeapi.app.requests.matchservicio.UpdateEstadoMatchServicioBody;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioDetailsResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioProveedorDetailsResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioResponse;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.exceptions.*;

import java.util.List;
import java.util.UUID;

public interface IMatchServicioService {
    List<MatchServicioProveedorDetailsResponse> obtenerDetailsFromCliente(UUID idCliente)
            throws DatabaseNotWorkingException, ResourceNotFoundException;

    List<MatchServicioDetailsResponse> obtenerDetailsFromPrestamistaByOptionalEstado(UUID idPrestamista, Estado estado)
            throws DatabaseNotWorkingException, ResourceNotFoundException;

    List<MatchServicioDetailsResponse> obtenerDetailsFromPrestamistaInServing(UUID idPrestamista)
            throws DatabaseNotWorkingException, ResourceNotFoundException;

    MatchServicioResponse registrar(CreateMatchServicioBody requestBody)
            throws DatabaseNotWorkingException, NotCreatedException;

    MatchServicioResponse actualizarEstado(UUID id, UpdateEstadoMatchServicioBody requestBody)
            throws DatabaseNotWorkingException, NotUpdatedException, ResourceNotFoundException, BadRequestException;

    MatchServicioResponse puntuarServicio(UUID id, PuntajeServicioRequest request)
            throws DatabaseNotWorkingException, NotUpdatedException;
}
