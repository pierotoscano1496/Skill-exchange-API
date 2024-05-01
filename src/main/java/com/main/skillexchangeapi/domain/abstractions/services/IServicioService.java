package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.requests.servicio.AsignacionModalidadPagoToServicioRequest;
import com.main.skillexchangeapi.app.requests.servicio.AsignacionRecursoMultimediaToServicioRequest;
import com.main.skillexchangeapi.app.requests.servicio.CreateServicioBody;
import com.main.skillexchangeapi.app.responses.servicio.ServicioModalidadesPagoAsignadosResponse;
import com.main.skillexchangeapi.app.responses.servicio.ServicioRecursosMultimediaAsignadosResponse;
import com.main.skillexchangeapi.app.responses.servicio.ServicioRegisteredResponse;
import com.main.skillexchangeapi.app.responses.servicio.ServicioResponse;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface IServicioService {
    List<ServicioResponse> obtenerByUsuario(UUID idUsuario) throws DatabaseNotWorkingException, ResourceNotFoundException;

    ServicioRegisteredResponse registrar(CreateServicioBody requestBody) throws DatabaseNotWorkingException, NotCreatedException;

    ServicioModalidadesPagoAsignadosResponse asignarModalidadesPago(UUID id, List<AsignacionModalidadPagoToServicioRequest> requestBody) throws DatabaseNotWorkingException, NotCreatedException;

    ServicioRecursosMultimediaAsignadosResponse asignarRecursosMultimedia(UUID id, List<AsignacionRecursoMultimediaToServicioRequest> requestBody) throws DatabaseNotWorkingException, NotCreatedException;
}
