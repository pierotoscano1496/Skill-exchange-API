package com.main.skillexchangeapi.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Parámetros incorrectos")
public class CustomExeption extends Exception{
    public CustomExeption(String message) {
        super(message);
    }
}
