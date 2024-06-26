package com.main.skillexchangeapi.domain.abstractions.services.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IAWSS3ChatService {
    String uploadFile(String keyName, MultipartFile multipartFile) throws IOException;
}
