package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.security.TokenUtils;
import com.main.skillexchangeapi.app.security.entities.UsuarioCredenciales;
import com.main.skillexchangeapi.domain.abstractions.security.services.IAuthService;
import com.main.skillexchangeapi.domain.abstractions.services.ITokenBlackList;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.domain.exceptions.UnsuccessLoginException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    private ITokenBlackList tokenBlackList;

    @Autowired
    private TokenUtils tokenUtils;

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

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletRequest request) {
        String token = tokenUtils.extractTokenFromRequest(request);
        tokenBlackList.addToBlacklist(token);

        return ResponseEntity.ok(true);
    }

    @GetMapping("/is-logged-in")
    public ResponseEntity<Boolean> isLoggedIn(HttpServletRequest request) {
        String token = tokenUtils.extractTokenFromRequest(request);
        boolean isBlacklisted = tokenBlackList.isBlacklisted(token);
        return ResponseEntity.ok(!isBlacklisted);
    }
}
