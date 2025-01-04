package com.main.skillexchangeapi.domain.abstractions.services.storage;

import com.main.skillexchangeapi.app.responses.servicio.MultimediaResourceUploadedResponse;
import com.main.skillexchangeapi.domain.exceptions.FileNotUploadedException;
import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import com.main.skillexchangeapi.domain.exceptions.MetadataInvalidException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IAWSS3ServicioService {
    List<MultimediaResourceUploadedResponse> uploadMultimediaServiceResources(UUID idServicio, List<MultipartFile> multipartFiles) throws IOException, InvalidFileException, FileNotUploadedException;

    String uploadModalidadPagoResource(UUID idServicio, MultipartFile multipartFiles) throws IOException, InvalidFileException, FileNotUploadedException;
}
