package com.main.skillexchangeapi.application.services.storage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.main.skillexchangeapi.app.responses.servicio.MultimediaResourceUploadedResponse;
import com.main.skillexchangeapi.app.utils.FileUitls;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.services.storage.IBlobStorageRecursoMultimediaServicioService;
import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BlobStorageRecursoMultimediaServicioService implements IBlobStorageRecursoMultimediaServicioService {
    @Value("${spring.cloud.azure.storage.blob.container-multimedia-resources}")
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
    public List<MultimediaResourceUploadedResponse> uploadMultimediaServiceResources(UUID idServicio, List<MultipartFile> multipartFiles) throws IOException, InvalidFileException {
        List<MultimediaResourceUploadedResponse> resourcesUploaded = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            Optional<String> fileExtension = FileUitls.getExtensionFromMultimediaResource(file.getOriginalFilename());
            if (fileExtension.isPresent()) {
                //String fileName = UuidManager.randomUuid() + "_" + LocalDateTime.now() + "." + fileExtension.get();
                String fileName = idServicio + "_"
                        + file.getOriginalFilename() + "_"
                        + UuidManager.randomUuid() + "." + fileExtension.get();
                BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName)
                        .getBlobClient(fileName);

                try {
                    blobClient.upload(file.getInputStream(), file.getSize(), true);

                    resourcesUploaded.add(MultimediaResourceUploadedResponse.builder()
                            .url(blobClient.getBlobUrl())
                            .medio(FileUitls.getMedioFromMultimediaResource(blobClient.getBlobUrl()).get())
                            .build());
                } catch (IOException e) {
                    throw new IOException("Error al guardar el archivo " + file.getOriginalFilename());
                }
            } else {
                throw new InvalidFileException("Archivo " + file.getOriginalFilename() + " no válido");
            }
        }

        return resourcesUploaded;
    }
}
