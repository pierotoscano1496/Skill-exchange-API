package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.security.entities.UsuarioCredenciales;
import com.main.skillexchangeapi.domain.abstractions.security.services.IAuthService;
import com.main.skillexchangeapi.domain.abstractions.services.IUsuarioService;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.domain.exceptions.UnsuccessLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "auth", produces = "application/json")
public class AuthController {
    @Autowired
    private IAuthService service;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UsuarioCredenciales credenciales) {
        try {
            String token = service.getTokenLogin(credenciales);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Authorization", "Bearer " + token)
                    .build();
        } catch (DatabaseNotWorkingException | ResourceNotFoundException | UnsuccessLoginException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error en la autenticación");
        }
    }
}
