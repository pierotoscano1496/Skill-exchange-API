package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.domain.abstractions.services.IMatchServicioService;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping(value = "match", produces = "application/json")
public class MatchServicioController {
    @Autowired
    private IMatchServicioService service;

    @PostMapping
    public MatchServicio registrar(@RequestBody MatchServicio matchServicio) {
        try {
            return service.registrar(matchServicio);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("estado/{id}")
    public MatchServicio actualizarEstado(@PathVariable UUID id, @RequestBody String estado) {
        try {
            return service.actualizarEstado(id, estado);
        } catch (DatabaseNotWorkingException | NotUpdatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
