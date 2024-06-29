package com.main.skillexchangeapi.application.services.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.main.skillexchangeapi.app.utils.FileUitls;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.services.storage.IAWSS3ChatService;
import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AWSS3ChatService implements IAWSS3ChatService {
    @Value("${aws.credentials.bucket-name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Override
    public String uploadFile(MultipartFile multipartFile, UUID idCoversation) throws IOException, InvalidFileException {
        Optional<String> fileExtension = FileUitls.getExtension(multipartFile.getOriginalFilename());
        if (fileExtension.isPresent()) {
            String pathFile = idCoversation.toString() + "/" + UuidManager.randomUuid() + "_" + LocalDateTime.now() + "." + fileExtension.get();
            s3Client.putObject(bucketName, pathFile, multipartFile.getInputStream(), null);
            return "https://" + bucketName + ".s3.amazonaws.com/" + pathFile;
        } else {
            throw new InvalidFileException("Archivo no válido");
        }
    }
}
