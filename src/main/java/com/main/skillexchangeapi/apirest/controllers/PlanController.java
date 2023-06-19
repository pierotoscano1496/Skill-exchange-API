package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.domain.abstractions.services.IPlanService;
import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "plan", produces = "application/json")
@CrossOrigin(origins = "${url.allowed.host}")
public class PlanController {
    @Autowired
    private IPlanService service;

    public PlanController(IPlanService service) {
        this.service = service;
    }

    @GetMapping
    public ArrayList<Plan> obtener() {
        try {
            return service.obtener();
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof ResourceNotFoundException) {
                status = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    @GetMapping("/codigo/{codigo}")
    public Plan obtenerPlan(@PathVariable String codigo) {
        try {
            return service.obtenerByCodigo(codigo);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof ResourceNotFoundException) {
                status = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }
}
