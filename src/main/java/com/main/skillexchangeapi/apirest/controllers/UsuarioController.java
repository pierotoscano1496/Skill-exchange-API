package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.requests.SetPlanToUsuarioRequest;
import com.main.skillexchangeapi.domain.abstractions.services.IPlanService;
import com.main.skillexchangeapi.domain.abstractions.services.IUsuarioService;
import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping(value = "usuario", produces = "application/json")
@CrossOrigin(origins = "${url.allowed.host}")
public class UsuarioController {
    @Autowired
    private IUsuarioService service;

    public UsuarioController(IUsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public Usuario registrar(@RequestBody Usuario usuario) {
        try {
            return service.registrar(usuario);
        } catch (NotCreatedException | EncryptionAlghorithmException | DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/plan/{id}")
    public PlanUsuario asignarPlan(@RequestParam UUID id, SetPlanToUsuarioRequest requestBody) {
        try {
            return service.asignarPlan(id, requestBody);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
