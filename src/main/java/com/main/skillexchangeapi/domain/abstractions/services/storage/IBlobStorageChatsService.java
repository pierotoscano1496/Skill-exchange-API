package com.main.skillexchangeapi.domain.abstractions.services.storage;

import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

public interface IBlobStorageChatsService {
    String uploadResource(MultipartFile multipartFile) throws IOException, InvalidFileException;
}
