package com.main.skillexchangeapi.apirest.controllers.storage;

import com.main.skillexchangeapi.domain.abstractions.services.storage.IBlobStorageChatsService;
import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping(value = "archivos", produces = "application/json")
public class FilesBlobStorageController {
    @Autowired
    private IBlobStorageChatsService service;

    @PostMapping("/upload")
    private String upload(@RequestParam("file") MultipartFile file) {
        try {
            return service.uploadResource(file);
        } catch (IOException | InvalidFileException e) {
            HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            String errorMessage = "Error de procesamiento de archivo";
            if (e instanceof InvalidFileException) {
                errorStatus = HttpStatus.BAD_REQUEST;
                errorMessage = "Archivo no válido";
            }
            throw new ResponseStatusException(errorStatus, errorMessage);
        }
    }
}
