package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.requests.usuario.AsignacionSkillToUsuarioRequest;
import com.main.skillexchangeapi.app.requests.SetPlanToUsuarioRequest;
import com.main.skillexchangeapi.app.requests.usuario.CreateUsuarioBody;
import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.app.responses.usuario.PlanAsignado;
import com.main.skillexchangeapi.app.responses.usuario.UsuarioRegisteredResponse;
import com.main.skillexchangeapi.app.responses.usuario.UsuarioSkillsAsignadosResponse;
import com.main.skillexchangeapi.app.security.TokenUtils;
import com.main.skillexchangeapi.app.security.services.InMemoryTokenBlackList;
import com.main.skillexchangeapi.domain.abstractions.services.ITokenBlackList;
import com.main.skillexchangeapi.domain.abstractions.services.IUsuarioService;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "usuario", produces = "application/json")
public class UsuarioController {
    @Autowired
    private IUsuarioService service;

    @Autowired
    private ITokenBlackList tokenBlackList;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping
    public ResponseEntity<UsuarioRegisteredResponse> obtener(HttpServletRequest request) {
        try {
            String correo = tokenUtils.extractEmailFromRequest(request);
            return ResponseEntity.status(HttpStatus.OK).body(service.obtener(correo));
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/skills/{id}")
    public List<SkillResponse> obtenerSkills(@PathVariable UUID id) {
        try {
            return service.obtenerSkills(id);
        } catch (DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<UsuarioRegisteredResponse> registrar(@RequestBody CreateUsuarioBody requestBody) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(requestBody));
        } catch (NotCreatedException | EncryptionAlghorithmException | DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/plan/{id}")
    public ResponseEntity<PlanAsignado> asignarPlan(@PathVariable UUID id, SetPlanToUsuarioRequest requestBody) {
        try {
            PlanAsignado planUsuarioAssigned = service.asignarPlan(id, requestBody);
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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = tokenUtils.extractTokenFromRequest(request);
        tokenBlackList.addToBlacklist(token);

        return ResponseEntity.ok("Logout succesfull");
    }
}
