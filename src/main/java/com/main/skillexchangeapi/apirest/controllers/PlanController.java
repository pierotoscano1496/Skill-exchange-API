package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.responses.PlanResponse;
import com.main.skillexchangeapi.domain.abstractions.services.IPlanService;
import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "plan", produces = "application/json")
//@CrossOrigin(origins = "${url.allowed.host}")
public class PlanController {
    @Autowired
    private IPlanService service;

    public PlanController(IPlanService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PlanResponse>> obtener() {
        try {
            List<PlanResponse> planes = service.obtener();
            return ResponseEntity.ok(planes);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof ResourceNotFoundException) {
                status = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Plan> obtenerPlan(@PathVariable String codigo) {
        try {
            Plan plan = service.obtenerByCodigo(codigo);
            return ResponseEntity.ok(plan);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof ResourceNotFoundException) {
                status = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(status, e.getMessage());
        }
    }
}
