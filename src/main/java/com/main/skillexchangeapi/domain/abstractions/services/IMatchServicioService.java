package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.requests.matchservicio.CreateMatchServicioBody;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioDetailsResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioProveedorDetailsResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioResponse;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.exceptions.*;

import java.util.List;
import java.util.UUID;

public interface IMatchServicioService {
    List<MatchServicioProveedorDetailsResponse> obtenerDetailsFromCliente(UUID idCliente) throws DatabaseNotWorkingException, ResourceNotFoundException;

    List<MatchServicioDetailsResponse> obtenerDetailsFromPrestamistaByOptionalEstado(UUID idPrestamista, String estado) throws DatabaseNotWorkingException, ResourceNotFoundException;

    MatchServicioResponse registrar(CreateMatchServicioBody requestBody) throws DatabaseNotWorkingException, NotCreatedException;

    MatchServicioResponse actualizarEstado(UUID id, String estado) throws DatabaseNotWorkingException, NotUpdatedException, ResourceNotFoundException, BadRequestException;

    MatchServicioResponse puntuarServicio(UUID id, int puntuacion) throws DatabaseNotWorkingException, NotUpdatedException;
}
