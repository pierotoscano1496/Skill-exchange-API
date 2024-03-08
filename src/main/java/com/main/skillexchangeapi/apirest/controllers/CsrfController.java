package com.main.skillexchangeapi.apirest.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/csrf")
public class CsrfController {
    @GetMapping("/v1")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
}
