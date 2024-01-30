package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.requests.usuario.AsignacionSkillToUsuarioRequest;
import com.main.skillexchangeapi.app.requests.SetPlanToUsuarioRequest;
import com.main.skillexchangeapi.app.responses.usuario.UsuarioSkillsAsignadosResponse;
import com.main.skillexchangeapi.domain.abstractions.services.IUsuarioService;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "usuario", produces = "application/json")
//@CrossOrigin(origins = "${url.allowed.host}")
public class UsuarioController {
    @Autowired
    private IUsuarioService service;

    @PostMapping
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioRegistered = service.registrar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRegistered);
        } catch (NotCreatedException | EncryptionAlghorithmException | DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/plan/{id}")
    public ResponseEntity<PlanUsuario> asignarPlan(@RequestParam UUID id, SetPlanToUsuarioRequest requestBody) {
        try {
            PlanUsuario planUsuarioAssigned = service.asignarPlan(id, requestBody);
            return ResponseEntity.ok(planUsuarioAssigned);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/skills/{id}")
    public UsuarioSkillsAsignadosResponse asignarSkills(@PathVariable UUID id, @RequestBody List<AsignacionSkillToUsuarioRequest> requestBody) {
        try {
            return service.asignarSkills(id, requestBody);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
