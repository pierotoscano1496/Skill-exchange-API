package com.main.skillexchangeapi.domain.abstractions.services.storage;

import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface IAWSS3ChatService {
    String uploadFile(MultipartFile multipartFile,UUID idCoversation) throws IOException, InvalidFileException;
}
