package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.requests.AsignacionModalidadPagoToServicioRequest;
import com.main.skillexchangeapi.app.requests.AsignacionRecursoMultimediaToServicioRequest;
import com.main.skillexchangeapi.app.responses.ModalidadPagoAsignadoToServicioResponse;
import com.main.skillexchangeapi.app.responses.RecursosMultimediaAsignadoToServicioResponse;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;

import java.util.List;
import java.util.UUID;

public interface IServicioService {
    Servicio registrar(Servicio servicio) throws DatabaseNotWorkingException, NotCreatedException;

    List<ModalidadPagoAsignadoToServicioResponse> asignarModalidadesPago(UUID id, List<AsignacionModalidadPagoToServicioRequest> requestBody) throws DatabaseNotWorkingException, NotCreatedException;

    List<RecursosMultimediaAsignadoToServicioResponse> asignarRecursosMultimedia(UUID id, List<AsignacionRecursoMultimediaToServicioRequest> requestBody) throws DatabaseNotWorkingException, NotCreatedException;
}
