package com.hinhnhumoi.filedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class StorageService {

    @Value("${storage.location}")
    private String rootLocation;

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket-name}")
    private String s3BucketName;

    @Autowired
    public StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void storeLocal(MultipartFile file) {
        try {
            String ext = Optional.ofNullable(file.getOriginalFilename())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(f.lastIndexOf(".")))
                    .orElse("");
            String newName = UUID.randomUUID() + ext;
            Files.createDirectories(Paths.get(rootLocation));
            Files.copy(file.getInputStream(), Paths.get(rootLocation, newName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public String storeS3(MultipartFile file) {
        try {
            String ext = Optional.ofNullable(file.getOriginalFilename())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(f.lastIndexOf(".")))
                    .orElse("");
            String newName = UUID.randomUUID() + ext;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3BucketName)
                    .key(newName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return newName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file in S3", e);
        }
    }
}