package com.ymmo.ymmoapi.controller;

import org.springframework.web.bind.annotation.*;

// TODO: Change the return data to DTO
@RestController
@RequestMapping("/api")
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
    String createProperty() {
        return "Create a new property";
    }

    @PatchMapping("/properties/{id}")
    String updateProperty(@PathVariable Long id) {
        return "Update property with id: " + id;
    }

    @DeleteMapping("/properties/{id}")
    String deleteProperty(@PathVariable Long id) {
        return "Delete property with id: " + id;
    }
}
