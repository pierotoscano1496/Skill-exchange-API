package com.main.skillexchangeapi.apirest.controllers.storage;

import com.main.skillexchangeapi.domain.abstractions.services.storage.IFirebaseFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "chat-media")
public class ChattingStorageController {
    @Autowired
    private IFirebaseFileService service;

    @PostMapping
    public Object upload(@RequestParam("file")MultipartFile multipartFile){
        
    }
}
