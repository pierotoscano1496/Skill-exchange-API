package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.responses.usuario.SubCategoriaResponse;
import com.main.skillexchangeapi.application.services.SubCategoriaService;
import com.main.skillexchangeapi.domain.abstractions.services.ISubCategoriaService;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "sub-categoria", produces = "application/json")
public class SubCategoriaController {
    @Autowired
    private ISubCategoriaService service;

    @GetMapping("/{idCategoria}")
    public List<SubCategoriaResponse> obtenerByCategoria(@PathVariable UUID idCategoria) {
        try {
            return service.obtenerByCategoria(idCategoria);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
