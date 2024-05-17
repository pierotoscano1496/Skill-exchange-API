package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.requests.CreateSkillRequest;
import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.domain.abstractions.services.ISkillService;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "skill", produces = "application/json")
public class SkillController {
    @Autowired
    private ISkillService service;

    @GetMapping
    public List<SkillResponse> obtener() {
        try {
            return service.obtener();
        } catch (DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/sub-categoria/{idSubcategoria}")
    public List<SkillResponse> obtenerBySubCategoria(@PathVariable UUID idSubcategoria) {
        try {
            return service.obtenerBySubCategoria(idSubcategoria);
        } catch (DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<SkillResponse> registrar(@RequestBody CreateSkillRequest requestBody) {
        try {
            SkillResponse skillRegistered = service.registrar(requestBody);
            return ResponseEntity.status(HttpStatus.CREATED).body(skillRegistered);
        } catch (NotCreatedException | DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
