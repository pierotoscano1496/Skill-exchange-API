package com.main.skillexchangeapi.apirest.controllers.storage;

import com.main.skillexchangeapi.domain.abstractions.services.storage.IAWSS3ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping(value = "chat-resources", produces = "application/json")
public class ChatResourcesController {
    @Autowired
    private IAWSS3ChatService service;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            return service.uploadFile(file.getOriginalFilename(), file);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al intentar subir archivo");
        }
    }
}
