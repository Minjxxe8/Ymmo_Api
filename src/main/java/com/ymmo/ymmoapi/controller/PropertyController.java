package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.dto.PropertyCreationDto;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.service.PropertyService;
import jakarta.annotation.Nullable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PropertyController {
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }


    @GetMapping("/properties")
    public ResponseEntity<?> getAllProperties() {
        try {
            return ResponseEntity.ok(propertyService.getAllProperties());
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @GetMapping("/properties/search")
    public ResponseEntity<?> searchProperties(@RequestParam String query, @Nullable @RequestParam Integer minPrice, @Nullable @RequestParam Integer maxPrice) {
        try {
            return ResponseEntity.ok(propertyService.search(query, minPrice, maxPrice));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @GetMapping("/properties/types")
    public ResponseEntity<?> getAllPropertiesType() {
        try {
            return ResponseEntity.ok(propertyService.getAllPropertiesType());
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @GetMapping("/properties/{id}")
    public ResponseEntity<?> getPropertyById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(propertyService.getPropertyResponseById(id));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @PostMapping("/properties")
    @PreAuthorize("hasRole('admin') or hasRole('agent')")
    public ResponseEntity<?> createProperty(@RequestBody PropertyCreationDto property) {
        try {
            return ResponseEntity.ok(propertyService.createProperty(property));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @PostMapping("/properties/{id}/pictures")
    @PreAuthorize("hasRole('admin') or hasRole('agent')")
    public ResponseEntity<?> postPropertyPictures(@PathVariable int id, @RequestParam("files") List<MultipartFile> file) {
        try {
            propertyService.postPropertyPictures(id, file);
            return ResponseEntity.ok(file.size() + " new picture have been uploaded for property with id : " + id);
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @DeleteMapping("/properties/{id}/pictures")
    @PreAuthorize("hasRole('admin') or hasRole('agent')")
    public ResponseEntity<?> deletePropertyPicture(@PathVariable int id, @Param("path") String path) {
        try {
            propertyService.deletePicture(id, path);
            return ResponseEntity.ok("Picture with the path :" + path + " has been deleted successfully");
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/properties/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('agent')")
    public ResponseEntity<?> updateProperty(@PathVariable int id, @RequestBody PropertyCreationDto property) {
        try {
            return ResponseEntity.ok(propertyService.updateProperty(id, property));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @DeleteMapping("/properties/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('agent')")
    public ResponseEntity<?> deleteProperty(@PathVariable int id) {
        try {
            return ResponseEntity.ok(propertyService.deleteProperty(id));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @PatchMapping("properties/{id}/status")
    @PreAuthorize("hasRole('admin') or hasRole('agent')")
    public ResponseEntity<?> changePropertyStatus(@PathVariable int id, @RequestBody boolean newStatus) {
        try {
            return ResponseEntity.ok(propertyService.changePropertyStatus(id, newStatus));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }
}
