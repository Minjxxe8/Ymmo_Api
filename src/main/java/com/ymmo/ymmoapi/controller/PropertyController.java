package com.ymmo.ymmoapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// TODO: Change the return data to DTO
@RestController
@RequestMapping("/api")
@CrossOrigin
public class PropertyController {

    @GetMapping("/properties")
    String getAllProperties() {
        return "All properties";
    }

    @GetMapping("/properties/{id}")
    String getPropertyById(@PathVariable Long id) {
        return "Property with id: " + id;
    }

    @PostMapping("/properties")
    @PreAuthorize("hasRole('admin') or hasRole('agent')")
    String createProperty() {
        return "Create a new property";
    }

    @PatchMapping("/properties/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('agent')")
    String updateProperty(@PathVariable Long id) {
        return "Update property with id: " + id;
    }

    @DeleteMapping("/properties/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('agent')")
    String deleteProperty(@PathVariable Long id) {
        return "Delete property with id: " + id;
    }
}
