package com.main.skillexchangeapi.app.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CreateBucketRequest;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${url.server}")
    private String urlServer;

    @Value("${aws.credentials.servicio-bucket-name}")
    private String bucketName;

    @Value("${aws.credentials.region:us-east-1}")
    private String region;

    @Autowired
    private AmazonS3 s3Client;

    @Override
    public void run(String... args) throws Exception {
        try {
            if (!s3Client.doesBucketExistV2(bucketName)) {
                if (region.equals("us-east-1")) {
                    s3Client.createBucket(bucketName);
                } else {
                    s3Client.createBucket(new CreateBucketRequest(bucketName, region));
                }

                for (int i = 0; i < 10 && !s3Client.doesBucketExistV2(bucketName); i++) {
                    Thread.sleep(3000);
                    logger.info("Esperando a que el bucket {} se cree...", bucketName);
                }
            }

            if (s3Client.doesBucketExistV2(bucketName)) {
                logger.info("Bucket {} creado exitosamente.", bucketName);
            } else {
                logger.error("No se pudo crear el bucket {} después de varios intentos.", bucketName);
            }

            logger.info("Servidor corriendo en: {}", urlServer);
        } catch (Exception ex) {
            logger.error("Error al iniciar la aplicación: {}", ex.getMessage(), ex);
        }
    }
}
