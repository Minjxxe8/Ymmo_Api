package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/picture")
@CrossOrigin
public class PictureController {
    private final S3Service s3Service;

    @Autowired
    public PictureController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok("File uploaded successfully add the address :" + s3Service.uploadFile(file));
    }
}
