package com.main.skillexchangeapi.application.services.storage;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AWSS3ServicioService implements IAWSS3ServicioService {
    private static final Logger logger = LogManager.getLogger(AWSS3ServicioService.class);

    @Value("${aws.credentials.servicio-bucket-name}")
    private String bucketName;

    @Value("${spring.profiles.active:prod}")
    private String profile;

    @Autowired
    private AmazonS3 s3Client;

    public String generatePresignedUrl(String objectKey) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 5; // 5 minutos
        expiration.setTime(expTimeMillis);

        // Crear la solicitud para generar la URL firmada
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
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
    public List<MultimediaResourceUploadedResponse> uploadMultimediaServiceResources(UUID idServicio,
            List<MultipartFile> multipartFiles) throws IOException, InvalidFileException, FileNotUploadedException {
        List<MultimediaResourceUploadedResponse> resourcesUploaded = new ArrayList<>();

        if (multipartFiles != null && multipartFiles.size() > 0) {
            for (MultipartFile file : multipartFiles) {
                Optional<String> fileExtension = FileUitls.getExtension(file.getOriginalFilename(),
                        ResourceSource.MULTIMEDIA);
                if (fileExtension.isPresent()) {
                    /*
                     * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                     * String uploadDate = LocalDateTime.now().format(formatter);
                     */

                    UUID idRecurso = UuidManager.randomUuid();
                    String fileName = idRecurso + "." + fileExtension.get();
                    String pathFile = idServicio.toString() + "/multimedia/" + fileName;

                    byte[] bytes = file.getBytes();
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(file.getSize());
                    metadata.setContentType(file.getContentType());

                    s3Client.putObject(bucketName, pathFile, byteArrayInputStream, metadata);

                    String url = getUrlResource(bucketName, pathFile);

                    resourcesUploaded.add(MultimediaResourceUploadedResponse.builder()
                            .idRecurso(idRecurso)
                            .url(url)
                            .medio(FileUitls.getMedioFromMultimediaResource(pathFile).get())
                            .build());
                } else {
                    throw new InvalidFileException("Archivo no válido");
                }
            }
        }

        return resourcesUploaded;
    }

    @Override
    public String uploadModalidadPagoResource(UUID idServicio, ModalidadPagoConstants.Tipo paymentMethod,
            MultipartFile multipartFiles) throws IOException, InvalidFileException, FileNotUploadedException {
        Optional<String> fileExtension = FileUitls.getExtension(multipartFiles.getOriginalFilename(),
                ResourceSource.PAYMENT);
        try {
            if (fileExtension.isPresent()) {
                String pathFolder = idServicio.toString() + "/payments/" + paymentMethod.toString();

                // Evaluar si ya existe un archivo en el bucket de yape y eliminarlo
                if (paymentMethod == ModalidadPagoConstants.Tipo.yape) {
                    ListObjectsV2Request request = new ListObjectsV2Request()
                            .withBucketName(bucketName)
                            .withPrefix(pathFolder);

                    ListObjectsV2Result result = s3Client.listObjectsV2(request);
                    List<S3ObjectSummary> objects = result.getObjectSummaries();

                    if (!objects.isEmpty()) {
                        for (S3ObjectSummary object : objects) {
                            s3Client.deleteObject(bucketName, object.getKey());
                        }
                    }
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String uploadDate = LocalDateTime.now().format(formatter);

                String fileName = UuidManager.randomUuid() + "_" + uploadDate + "." + fileExtension.get();
                String pathFile = pathFolder + "/" + fileName;
                s3Client.putObject(bucketName, pathFile, multipartFiles.getInputStream(), null);
                return getUrlResource(bucketName, pathFile);
            } else {
                throw new InvalidFileException("Archivo no válido");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new FileNotUploadedException("Error al subir archivo a S3");
        }

    }

    private String getUrlResource(String bucketName, String pathFile) {
        if (profile.equals("dev")) {
            String url = "http://localhost:4566/" + bucketName + "/" + pathFile;
            logger.info("Endpoint localstack S3: {}", url);
            return url;
        }
        return "https://" + bucketName + ".s3.amazonaws.com/" + pathFile;
    }

    @Override
    public String getFirstImageServicioPresignedUrl(UUID idServicio) throws FileNotFoundException {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(idServicio.toString() + "/multimedia/");

        ListObjectsV2Result result = s3Client.listObjectsV2(request);
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        Optional<S3ObjectSummary> firstImage = objects.stream()
                .filter(obj -> FileUitls.checkFileType(obj.getKey(), ResourceSource.MULTIMEDIA, ResourceType.IMAGE))
                .findFirst();

        if (firstImage.isEmpty()) {
            throw new FileNotFoundException("Imagen no encontrada");
        }

        return generatePresignedUrl(firstImage.get().getKey());
    }

    @Override
    public String getImageMetodoPagoPresignedUrl(UUID idServicio,
            ModalidadPagoConstants.Tipo paymentMethod)
            throws FileNotFoundException {
        String pathFolder = idServicio.toString() + "/payments/" + paymentMethod.toString();

        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(pathFolder);

        ListObjectsV2Result result = s3Client.listObjectsV2(request);
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        Optional<S3ObjectSummary> firstImage = objects.stream()
                .filter(obj -> FileUitls.checkFileType(obj.getKey(), ResourceSource.MULTIMEDIA, ResourceType.IMAGE))
                .findFirst();

        if (firstImage.isEmpty()) {
            throw new FileNotFoundException("Imagen no encontrada");
        }

        return generatePresignedUrl(firstImage.get().getKey());
    }

    @Override
    public void deleteRecursoMultimediaServicio(UUID idServicio, List<UUID> listIdRecursos)
            throws FileNotFoundException {
        if (listIdRecursos == null || listIdRecursos.isEmpty()) {
            return;
        }

        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(idServicio.toString() + "/multimedia/");
        ListObjectsV2Result result = s3Client.listObjectsV2(request);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        Map<UUID, String> recursoMap = new HashMap<>();

        for (S3ObjectSummary object : objects) {
            String idRecursoStr = FileUitls.getFileNameWithoutExtension(object.getKey());
            UUID idRecurso = UUID.fromString(idRecursoStr);
            recursoMap.put(idRecurso, object.getKey());
        }

        for (UUID idRecurso : listIdRecursos) {
            if (recursoMap.containsKey(idRecurso)) {
                String objectKey = recursoMap.get(idRecurso);
                s3Client.deleteObject(bucketName, objectKey);
            } else {
                throw new FileNotFoundException("Recurso multimedia con ID " + idRecurso + " no encontrado en S3");
            }
        }

    }

    @Override
    public void deleteModalidadPagoYapeResource(UUID idServicio) throws FileNotFoundException {
        String pathFolder = idServicio.toString() + "/payments/" + ModalidadPagoConstants.Tipo.yape.toString();

        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(pathFolder);

        ListObjectsV2Result result = s3Client.listObjectsV2(request);
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        if (!objects.isEmpty()) {
            for (S3ObjectSummary object : objects) {
                s3Client.deleteObject(bucketName, object.getKey());
            }
        }
    }
}
