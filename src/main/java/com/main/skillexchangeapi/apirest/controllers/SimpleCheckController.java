package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.domain.entities.CheckInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "simple-check", produces = "application/json")
@CrossOrigin
public class SimpleCheckController {

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    @PostMapping
    public ResponseEntity<String> helloName(@RequestBody CheckInfo checkInfo) {
        return ResponseEntity.ok("Hello: " + checkInfo.getNombre());
    }
}
