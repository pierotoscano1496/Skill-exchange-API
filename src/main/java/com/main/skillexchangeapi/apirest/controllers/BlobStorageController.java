package com.main.skillexchangeapi.apirest.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

@RestController
@RequestMapping(name = "blob")
public class BlobStorageController {
    @Value("azure-blob://testcontainer/test.txt")
    private Resource blobFile;

    @GetMapping("/read")
    public String read() throws IOException{
        return StreamUtils.copyToString(blobFile.getInputStream(), Charset.defaultCharset());
    }

    @PostMapping("/write")
    public String write(@RequestBody String data) throws IOException {
        try (OutputStream os=((WritableResource)blobFile).getOutputStream()) {
            os.write(data.getBytes());
        }

        return "Archivo actualizado";
    }
}
