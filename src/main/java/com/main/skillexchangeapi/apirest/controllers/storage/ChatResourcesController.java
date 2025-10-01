package com.main.skillexchangeapi.apirest.controllers.storage;

import com.main.skillexchangeapi.domain.abstractions.services.storage.IAWSS3ChatService;
import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "chat-resources", produces = "application/json")
public class ChatResourcesController {
    @Autowired
    private IAWSS3ChatService service;

    @PutMapping("/upload/{idConversation}")
    public String upload(@RequestParam("file") MultipartFile file, @PathVariable UUID idConversation) {
        try {
            return service.uploadFile(file, idConversation);
        } catch (IOException | InvalidFileException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al intentar subir archivo");
        }
    }
}
