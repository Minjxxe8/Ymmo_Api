package com.ymmo.ymmoapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {
    private final S3Client s3Client;
    private final String bucketName;
    private final String port;

    @Autowired
    public S3Service(
            S3Client s3Client,
            @Value("${bucket.name}") String bucketName,
            @Value("${s3.endpoint}") String endpoint) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.port = endpoint.substring(endpoint.lastIndexOf(':'));
    }


    public String uploadFile(MultipartFile file) throws IOException {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes()));

        return "http://localhost" + port + "/" + bucketName + "/" + key;
    }

    public void deleteFile(String path) throws IOException {
        String key = path.substring(path.lastIndexOf('/')+1);
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());
    }
}
