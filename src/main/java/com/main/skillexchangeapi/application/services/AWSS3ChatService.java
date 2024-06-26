package com.main.skillexchangeapi.application.services;

import com.amazonaws.services.s3.AmazonS3;
import com.main.skillexchangeapi.domain.abstractions.services.storage.IAWSS3ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AWSS3ChatService implements IAWSS3ChatService {
    @Value("${aws.credentials.bucket-name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Override
    public String uploadFile(String keyName, MultipartFile multipartFile) throws IOException {
        s3Client.putObject(bucketName, keyName, multipartFile.getInputStream(), null);
        return "Archivo subido exitosamente";
    }
}
