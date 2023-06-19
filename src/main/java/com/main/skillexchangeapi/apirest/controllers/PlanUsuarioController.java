package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.domain.abstractions.services.IPlanUsuarioService;
import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "plan-usuario", produces = "application/json")
@CrossOrigin(origins = "${url.allowed.host}")
public class PlanUsuarioController {
    @Autowired
    private IPlanUsuarioService service;

    public PlanUsuarioController(IPlanUsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public PlanUsuario registrar(@RequestBody PlanUsuario planUsuario) {
        try {
            return service.registrar(planUsuario);
        } catch (NotCreatedException | DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
