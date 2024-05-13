package com.main.skillexchangeapi.domain.abstractions.services.storage;

import com.main.skillexchangeapi.app.responses.servicio.MultimediaResourceUploadedResponse;
import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IBlobStorageModalidadPagoService {
    String uploadMetadataToService(UUID idServicio, MultipartFile multipartFile) throws IOException, InvalidFileException;
}
