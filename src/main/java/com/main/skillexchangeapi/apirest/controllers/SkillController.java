package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.application.services.SkillService;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "skill", produces = "application/json")
@CrossOrigin(origins = "${url.allowed.host}")
public class SkillController {
    @Autowired
    private SkillService service;

    @PostMapping
    public Skill registrar(@RequestBody Skill skill) {
        try {
            return service.registrar(skill);
        } catch (NotCreatedException | DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
