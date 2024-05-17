package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.requests.matchservicio.CreateMatchServicioBody;
import com.main.skillexchangeapi.app.requests.matchservicio.PuntajeServicioRequest;
import com.main.skillexchangeapi.app.requests.matchservicio.UpdateEstadoMatchServicioBody;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioDetailsResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioEstadoUpdatedResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioProveedorDetailsResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioResponse;
import com.main.skillexchangeapi.domain.abstractions.services.IMatchServicioService;
import com.main.skillexchangeapi.domain.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "match", produces = "application/json")
public class MatchServicioController {
    @Autowired
    private IMatchServicioService service;

    @GetMapping("details/cliente/{idCliente}")
    public List<MatchServicioProveedorDetailsResponse> obtenerDetailsFromCliente(@PathVariable UUID idCliente) {
        try {
            return service.obtenerDetailsFromCliente(idCliente);
        } catch (ResourceNotFoundException | DatabaseNotWorkingException e) {
            HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof ResourceNotFoundException) {
                errorStatus = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(errorStatus, e.getMessage());
        }
    }

    @GetMapping({"details/prestamista/{idPrestamista}/estado/{estado}", "details/prestamista/{idPrestamista}"})
    public List<MatchServicioDetailsResponse> obtenerDetailsFromPrestamistaByOptionalEstado(@PathVariable UUID idPrestamista, @PathVariable(required = false) String estado) {
        try {
            return service.obtenerDetailsFromPrestamistaByOptionalEstado(idPrestamista, estado);
        } catch (ResourceNotFoundException | DatabaseNotWorkingException e) {
            HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof ResourceNotFoundException) {
                errorStatus = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(errorStatus, e.getMessage());
        }
    }

    @GetMapping("details/prestamista/{idPrestamista}/serving")
    public List<MatchServicioDetailsResponse> obtenerDetailsFromPrestamistaInServing(@PathVariable UUID idPrestamista) {
        try {
            return service.obtenerDetailsFromPrestamistaInServing(idPrestamista);
        } catch (ResourceNotFoundException | DatabaseNotWorkingException e) {
            HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof ResourceNotFoundException) {
                errorStatus = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(errorStatus, e.getMessage());
        }
    }

    @PostMapping
    public MatchServicioResponse registrar(@RequestBody CreateMatchServicioBody requestBody) {
        try {
            return service.registrar(requestBody);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("estado/{id}")
    public MatchServicioResponse actualizarEstado(@PathVariable UUID id, @RequestBody UpdateEstadoMatchServicioBody requestBody) {
        try {
            return service.actualizarEstado(id, requestBody);
        } catch (ResourceNotFoundException | DatabaseNotWorkingException | NotUpdatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("puntuacion/{id}")
    public MatchServicioResponse puntuarServicio(@PathVariable UUID id, @RequestBody PuntajeServicioRequest request) {
        try {
            return service.puntuarServicio(id, request);
        } catch (DatabaseNotWorkingException | NotUpdatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
