package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.requests.servicio.AsignacionModalidadPagoToServicioRequest;
import com.main.skillexchangeapi.app.requests.servicio.AsignacionRecursoMultimediaToServicioRequest;
import com.main.skillexchangeapi.app.requests.servicio.CreateServicioBody;
import com.main.skillexchangeapi.app.requests.servicio.SearchServiciosParametersBody;
import com.main.skillexchangeapi.app.requests.servicio.UpdateServicioBody;
import com.main.skillexchangeapi.app.responses.servicio.*;
import com.main.skillexchangeapi.app.responses.servicio.ServicioModalidadesPagoAsignadosResponse;
import com.main.skillexchangeapi.app.responses.servicio.ServicioRecursosMultimediaAsignadosResponse;
import com.main.skillexchangeapi.app.responses.servicio.ServicioRegisteredResponse;
import com.main.skillexchangeapi.app.responses.servicio.ServicioResponse;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.exceptions.BadRequestException;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.FileNotUploadedException;
import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public interface IServicioService {

        List<ServicioResponse> obtenerByProveedor(UUID idProveedor)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;

        List<ServicioResponse> searchByParameters(SearchServiciosParametersBody requestBody)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;

        ServicioResponse obtenerDetailsPreview(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException;

        ServicioRegisteredResponse registrar(CreateServicioBody requestBody, List<MultipartFile> recursosMultimedia,
                        MultipartFile yapeFile)
                        throws DatabaseNotWorkingException, NotCreatedException, IOException, InvalidFileException,
                        FileNotUploadedException;

        ServicioResponse actualizar(UUID id, UpdateServicioBody requestBody, List<MultipartFile> recursosMultimedia,
                        MultipartFile yapeFile)
                        throws DatabaseNotWorkingException, NotUpdatedException, BadRequestException,
                        IOException, InvalidFileException, FileNotUploadedException;

        ServicioModalidadesPagoAsignadosResponse asignarModalidadesPago(UUID id,
                        List<AsignacionModalidadPagoToServicioRequest> requestBody)
                        throws DatabaseNotWorkingException, NotCreatedException;

        ServicioRecursosMultimediaAsignadosResponse asignarRecursosMultimedia(UUID id,
                        List<AsignacionRecursoMultimediaToServicioRequest> requestBody)
                        throws DatabaseNotWorkingException, NotCreatedException;
}
