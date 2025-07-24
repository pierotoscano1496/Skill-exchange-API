package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.responses.usuario.CategoriaResponse;
import com.main.skillexchangeapi.domain.abstractions.services.ICategoriaService;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "categoria", produces = "application/json")
public class CategoriaController {
    @Autowired
    private ICategoriaService service;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> obtener() {
        try {
            List<CategoriaResponse> categorias = service.obtener();
            return ResponseEntity.ok(categorias);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("details")
    public ResponseEntity<List<CategoriaResponse>> obtenerDetails() {
        Logger logger = LoggerFactory.getLogger(CategoriaController.class);
        logger.info("Iniciando petición GET /categoria/details");

        try {
            List<CategoriaResponse> categorias = service.obtenerDetails();
            logger.info("Petición GET /categoria/details exitosa. Total categorías: {}", categorias.size());
            return ResponseEntity.ok(categorias);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            logger.error("Error en GET /categoria/details: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado en GET /categoria/details: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener detalles de las categorías");
        }
    }
}
