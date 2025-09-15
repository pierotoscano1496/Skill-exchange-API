package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;
import com.main.skillexchangeapi.app.requests.matchservicio.AcceptMatchServicioBody;
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

        List<MatchServicioDetailsResponse> obtenerDetailsFromProveedorAndOptionalEstado(UUID idProveedor,
                        Estado estado)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;

        List<MatchServicioDetailsResponse> obtenerDetailsFromProveedorInServing(UUID idProveedor)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;

        Double obtenerPuntajeFromProveedor(UUID idProveedor) throws DatabaseNotWorkingException;

        MatchServicioResponse registrar(CreateMatchServicioBody requestBody)
                        throws DatabaseNotWorkingException, NotCreatedException;

        MatchServicioResponse aceptarMatch(UUID id, AcceptMatchServicioBody requestBody)
                        throws DatabaseNotWorkingException, NotUpdatedException, ResourceNotFoundException,
                        BadRequestException;

        MatchServicioResponse actualizarEstado(UUID id, UpdateEstadoMatchServicioBody requestBody)
                        throws DatabaseNotWorkingException, NotUpdatedException, ResourceNotFoundException,
                        BadRequestException;

        MatchServicioResponse puntuarServicio(UUID id, PuntajeServicioRequest request)
                        throws DatabaseNotWorkingException, NotUpdatedException;
}
