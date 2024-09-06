package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.security.TokenUtils;
import com.main.skillexchangeapi.domain.abstractions.services.ITestingService;
import com.main.skillexchangeapi.domain.entities.TestingModel;
import com.main.skillexchangeapi.domain.entities.testing.TestingMensajeOptional;
import com.main.skillexchangeapi.domain.exceptions.CustomExeption;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping(value = "testing", produces = "application/json")
public class TestingController {
    @Autowired
    private ITestingService service;

    @Autowired
    private TokenUtils tokenUtils;

    @PostMapping
    public TestingModel registrar(@RequestBody TestingModel testingModel) {
        try {
            return service.registrar(testingModel);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("{id}")
    public TestingModel obtener(@PathVariable UUID id) {
        try {
            return service.obtener(id);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("ex/{number}")
    public HashMap<String, String> review(@PathVariable int number) {
        try {
            if (number > 0) {
                HashMap<String, String> map = new HashMap<>();
                map.put("mum", String.valueOf(number));
                map.put("estado", "Aceptado");
                return map;
            } else {
                throw new CustomExeption("Número no admitido");
            }
        } catch (CustomExeption ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error de número", ex);
        }
    }

    @PatchMapping("nombre/{id}")
    public TestingModel actualizarNombre(@PathVariable UUID id, @RequestBody TestingModel testingModel) {
        testingModel.setNombre("Cancelado");
        return testingModel;
    }


    // Validar HTTP Status lanzando excepción
    @PostMapping("mensajear")
    public String mensajear(@RequestBody String mensaje, HttpServletRequest request) {
        if (mensaje.equalsIgnoreCase("hola")) {
            String correo = tokenUtils.extractEmailFromRequest(request);
            return mensaje + correo;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Especifica el nombre");
        }
    }

    // Validar entradas opcionales del body
    @PostMapping("mensajear-opcional")
    public String mensajear(@RequestBody TestingMensajeOptional mensaje) {
        return "Tu saludo es: " + mensaje.getSaludo() + ", " + mensaje.getNombre();
    }

    /*
    @GetMapping("{nombre}")
    public String leer(@PathVariable String nombre) {
        return "Hola " + nombre;
    }

     */
}
