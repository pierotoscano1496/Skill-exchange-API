package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.responses.usuario.CategoriaResponse;
import com.main.skillexchangeapi.domain.abstractions.services.ICategoriaService;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping(value = "categoria", produces = "application/json")
public class CategoriaController {
    @Autowired
    private ICategoriaService service;

    @GetMapping
    public List<CategoriaResponse> obtener() {
        try {
            return service.obtener();
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
