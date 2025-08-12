package com.main.skillexchangeapi.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
@Profile("dev")
public class S3ConfigDev {
    @Value("${aws.credentials.access-key}")
    private String accessKey;

    @Value("${aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.endpoint}")
    private String endpoint;

    @Value("${aws.credentials.region:us-east-1}")
    private String region;

    @Bean
    public AmazonS3 s3Client() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Initializing Amazon S3 client with endpoint: {}", endpoint);
        logger.info("Using access key: {}", accessKey);
        logger.info("Using secret key: {}", secretKey);
        if (accessKey == null || secretKey == null || endpoint == null) {
            logger.error("AWS credentials or endpoint are not set properly.");
            throw new IllegalArgumentException("AWS credentials or endpoint are not set properly.");
        }

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withPathStyleAccessEnabled(true)
                .build();
    }
}
