package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.dto.PropertyCreationDto;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.service.PropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/properties/{id}")
    public ResponseEntity<?> getPropertyById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(propertyService.getPropertyById(id));
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
}
