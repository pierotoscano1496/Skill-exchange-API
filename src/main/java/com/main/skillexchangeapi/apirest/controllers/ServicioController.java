package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.requests.servicio.AsignacionModalidadPagoToServicioRequest;
import com.main.skillexchangeapi.app.requests.servicio.AsignacionRecursoMultimediaToServicioRequest;
import com.main.skillexchangeapi.app.requests.servicio.SearchServiciosParametersBody;
import com.main.skillexchangeapi.app.requests.servicio.CreateServicioBody;
import com.main.skillexchangeapi.app.responses.servicio.*;
import com.main.skillexchangeapi.domain.abstractions.services.IServicioService;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "servicio", produces = "application/json")
public class ServicioController {
    @Autowired
    private IServicioService service;

    @GetMapping("usuario/{idUsuario}")
    private List<ServicioResponse> obtenerByUsuario(UUID idUsuario) {
        try {
            return service.obtenerByUsuario(idUsuario);
        } catch (ResourceNotFoundException | DatabaseNotWorkingException e) {
            HttpStatus statusError = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof DatabaseNotWorkingException) {
                statusError = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(statusError, e.getMessage());
        }
    }

    @GetMapping("details/preview/{id}")
    public ServicioDetailsPreviewResponse obtenerDetailsPreview(UUID id) {
        try {
            return service.obtenerDetailsPreview(id);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;

            if (e instanceof ResourceNotFoundException) {
                errorStatus = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(errorStatus, e.getMessage());
        }
    }

    /**
     * Búsquedas personalizadas
     */
    @PostMapping("busqueda")
    private List<ServicioBusquedaResponse> searchByParameters(@RequestBody SearchServiciosParametersBody parameters) {
        try {
            return service.searchByParameters(parameters);
        } catch (DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ResourceNotFoundException e) {
            return new ArrayList<>();
        }
    }

    @PostMapping
    private ServicioRegisteredResponse registrar(@RequestBody CreateServicioBody requestBody) {
        try {
            return service.registrar(requestBody);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("modalidad-pago/{id}")
    private ServicioModalidadesPagoAsignadosResponse asignarModalidadesPago(@PathVariable UUID id, @RequestBody List<AsignacionModalidadPagoToServicioRequest> requestBody) {
        try {
            return service.asignarModalidadesPago(id, requestBody);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("recursos-multimedia/{id}")
    private ServicioRecursosMultimediaAsignadosResponse asignarRecursosMultimedia(@PathVariable UUID id, @RequestBody List<AsignacionRecursoMultimediaToServicioRequest> requestBody) {
        try {
            return service.asignarRecursosMultimedia(id, requestBody);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
