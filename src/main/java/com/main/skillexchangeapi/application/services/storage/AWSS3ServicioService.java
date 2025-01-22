package com.main.skillexchangeapi.application.services.storage;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.main.skillexchangeapi.app.responses.servicio.MultimediaResourceUploadedResponse;
import com.main.skillexchangeapi.app.utils.FileUitls;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.services.storage.IAWSS3ServicioService;
import com.main.skillexchangeapi.domain.constants.PaymentMethod;
import com.main.skillexchangeapi.domain.constants.ResourceSource;
import com.main.skillexchangeapi.domain.constants.ResourceType;
import com.main.skillexchangeapi.domain.exceptions.FileNotFoundException;
import com.main.skillexchangeapi.domain.exceptions.FileNotUploadedException;
import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AWSS3ServicioService implements IAWSS3ServicioService {
    private static final Logger logger = LogManager.getLogger(AWSS3ServicioService.class);

    @Value("${aws.credentials.servicio-bucket-name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public String generatePresignedUrl(String objectKey) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 5; // 5 minutos
        expiration.setTime(expTimeMillis);

        // Crear la solicitud para generar la URL firmada
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        // Generar la URL firmada
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    public String getFirstImagePresignedUrl(UUID idServicio) throws FileNotFoundException {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(idServicio.toString() + "/multimedia/")
                .withMaxKeys(1);

        ListObjectsV2Result result = s3Client.listObjectsV2(request);
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        if (objects.isEmpty()) {
            throw new FileNotFoundException("Imagen no encontrada");
        }

        String firstImageKey = objects.get(0).getKey();

        return generatePresignedUrl(firstImageKey);
    }

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

    @Override
    public String getFirstImageServicioPresignedUrl(UUID idServicio) throws FileNotFoundException {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(idServicio.toString() + "/multimedia/");

        ListObjectsV2Result result = s3Client.listObjectsV2(request);
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        Optional<S3ObjectSummary> firstImage = objects.stream().filter(obj -> FileUitls.checkFileType(obj.getKey(), ResourceSource.MULTIMEDIA, ResourceType.IMAGE)).findFirst();

        if (firstImage.isEmpty()) {
            throw new FileNotFoundException("Imagen no encontrada");
        }

        return generatePresignedUrl(firstImage.get().getKey());
    }

    @Override
    public String getImageMetodoPagoPresignedUrl(UUID idServicio, PaymentMethod paymentMethod) throws FileNotFoundException {
        String pathFolder = idServicio.toString() + "/payments/" + paymentMethod.getDisplayName();

        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(pathFolder);

        ListObjectsV2Result result = s3Client.listObjectsV2(request);
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        Optional<S3ObjectSummary> firstImage = objects.stream().filter(obj -> FileUitls.checkFileType(obj.getKey(), ResourceSource.MULTIMEDIA, ResourceType.IMAGE)).findFirst();

        if (firstImage.isEmpty()) {
            throw new FileNotFoundException("Imagen no encontrada");
        }

        return generatePresignedUrl(firstImage.get().getKey());
    }
}
