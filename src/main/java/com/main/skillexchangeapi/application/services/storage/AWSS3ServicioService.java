package com.main.skillexchangeapi.application.services.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.main.skillexchangeapi.app.responses.servicio.MultimediaResourceUploadedResponse;
import com.main.skillexchangeapi.app.utils.FileUitls;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.services.storage.IAWSS3ServicioService;
import com.main.skillexchangeapi.domain.constants.ResourceSource;
import com.main.skillexchangeapi.domain.exceptions.FileNotUploadedException;
import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AWSS3ServicioService implements IAWSS3ServicioService {
    private static final Logger logger = LogManager.getLogger(AWSS3ServicioService.class);


    @Value("${aws.credentials.servicio-bucket-name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Override
    public List<MultimediaResourceUploadedResponse> uploadMultimediaServiceResources(UUID idServicio, List<MultipartFile> multipartFiles) throws IOException, InvalidFileException, FileNotUploadedException {
        List<MultimediaResourceUploadedResponse> resourcesUploaded = new ArrayList<>();

        try {
            for (MultipartFile file : multipartFiles) {
                Optional<String> fileExtension = FileUitls.getExtension(file.getOriginalFilename(), ResourceSource.MULTIMEDIA);
                if (fileExtension.isPresent()) {
                    String fileName = UuidManager.randomUuid() + "_" + LocalDateTime.now() + "." + fileExtension.get();
                    String pathFile = idServicio.toString() + "/multimedia/" + fileName;
                    s3Client.putObject(bucketName, pathFile, file.getInputStream(), null);
                    resourcesUploaded.add(MultimediaResourceUploadedResponse.builder()
                            .url("https://" + bucketName + ".s3.amazonaws.com/" + pathFile)
                            .medio(FileUitls.getMedioFromMultimediaResource(pathFile).get())
                            .build());
                } else {
                    throw new InvalidFileException("Archivo no válido");
                }
            }

            return resourcesUploaded;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new FileNotUploadedException("Error al subir archivo a S3");
        }

    }

    @Override
    public String uploadModalidadPagoResource(UUID idServicio, MultipartFile multipartFiles) throws IOException, InvalidFileException, FileNotUploadedException {
        Optional<String> fileExtension = FileUitls.getExtension(multipartFiles.getOriginalFilename(), ResourceSource.PAYMENT);
        try {
            if (fileExtension.isPresent()) {
                String fileName = UuidManager.randomUuid() + "_" + LocalDateTime.now() + "." + fileExtension.get();
                String pathFile = idServicio.toString() + "/payments/" + fileName;
                s3Client.putObject(bucketName, pathFile, multipartFiles.getInputStream(), null);
                return "https://" + bucketName + ".s3.amazonaws.com/" + pathFile;
            } else {
                throw new InvalidFileException("Archivo no válido");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new FileNotUploadedException("Error al subir archivo a S3");
        }

    }
}
