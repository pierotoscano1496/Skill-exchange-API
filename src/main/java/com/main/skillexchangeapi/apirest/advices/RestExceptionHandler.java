package com.main.skillexchangeapi.apirest.advices;

import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.ResponseEntity.notFound;

@RestControllerAdvice
public class RestExceptionHandler {
    private static final Logger logger = LogManager.getLogger(RestExceptionHandler.class.getName());

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity resourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        logger.debug("handling ResourceNotFoundException");
        return notFound().build();
    }
}
