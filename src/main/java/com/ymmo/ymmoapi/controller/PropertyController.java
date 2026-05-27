package com.ymmo.ymmoapi.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

// TODO: Change the return data to DTO
public class PropertyController {

    @GetMapping("/properties")
    String getAllProperties() {
        return "All properties";
    }

    @GetMapping("/properties/{id}")
    String getPropertyById(Long id) {
        return "Property with id: " + id;
    }

    @PostMapping("/properties")
    String createProperty() {
        return "Create a new property";
    }

    @PatchMapping("/properties/{id}")
    String updateProperty(Long id) {
        return "Update property with id: " + id;
    }

    @DeleteMapping("/properties/{id}")
    String deleteProperty(Long id) {
        return "Delete property with id: " + id;
    }
}
