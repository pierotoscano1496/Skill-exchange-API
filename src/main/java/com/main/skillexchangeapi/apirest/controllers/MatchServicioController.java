package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.requests.matchservicio.CreateMatchServicioBody;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioEstadoUpdatedResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioResponse;
import com.main.skillexchangeapi.domain.abstractions.services.IMatchServicioService;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
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

    @GetMapping({"/prestamista/{idPrestamista}/estado/{estado}", "prestamista/{idPrestamista}"})
    public List<MatchServicioResponse> obtenerFromPrestamistaByOptionalEstado(@PathVariable UUID idPrestamista, @PathVariable(required = false) String estado) {
        try {
            return service.obtenerFromPrestamistaByOptionalEstado(idPrestamista, estado);
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
    public MatchServicioResponse actualizarEstado(@PathVariable UUID id, @RequestBody String estado) {
        try {
            return service.actualizarEstado(id, estado);
        } catch (DatabaseNotWorkingException | NotUpdatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("puntuacion/{id}")
    public MatchServicioResponse puntuarServicio(@PathVariable UUID id, @RequestBody int puntuacion) {
        try {
            return service.puntuarServicio(id, puntuacion);
        } catch (DatabaseNotWorkingException | NotUpdatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
