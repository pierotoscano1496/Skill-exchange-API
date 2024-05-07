package com.main.skillexchangeapi.application.services.storage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.main.skillexchangeapi.app.utils.FileUitls;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.services.storage.IBlobStorageChatsService;
import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BlobStorageChatsService implements IBlobStorageChatsService {
    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    @Value("${azure.blob-storage.connection-string}")
    private String connectionString;

    private BlobServiceClient blobServiceClient;

    @PostConstruct
    public void init() {
        blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }

    @Override
    public String uploadResource(MultipartFile multipartFile) throws IOException, InvalidFileException {
        Optional<String> fileExtension = FileUitls.getExtension(multipartFile.getOriginalFilename());
        if (fileExtension.isPresent()) {
            String fileName = UuidManager.randomUuid() + "_" + LocalDateTime.now() + "." + fileExtension.get();
            BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName)
                    .getBlobClient(fileName);

            blobClient.upload(multipartFile.getInputStream(), multipartFile.getSize(), true);

            String fileUrl = blobClient.getBlobUrl();

            return fileUrl;
        } else {
            throw new InvalidFileException("Archivo no válido");
        }

    }
}
