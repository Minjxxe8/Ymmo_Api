package com.ymmo.ymmoapi.service;

import com.ymmo.ymmoapi.dto.PropertyCreationDto;
import com.ymmo.ymmoapi.exception.ResourceNotFoundException;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.model.Properties;
import com.ymmo.ymmoapi.model.PropertiesBuilder;
import com.ymmo.ymmoapi.repository.PropertiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class PropertyService {
    private final PropertiesRepository propertiesRepository;

    @Autowired
    public PropertyService(PropertiesRepository propertiesRepository) {
        this.propertiesRepository = propertiesRepository;
    }

    public List<Properties> getAllProperties() throws ResourceNotFoundException {
        List<Properties> propertiesList = propertiesRepository.findAll();
        if (propertiesList.isEmpty()) {
            throw new ResourceNotFoundException("No properties have been created");
        }
        return propertiesList;
    }

    public Properties getPropertyById(int id) throws ResourceNotFoundException {
        return propertiesRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Property with the id " + id + " has not been found"))
                .getBody();
    }

    public Properties createProperty(PropertyCreationDto property) throws ResponseException {
        try {
            return propertiesRepository.save(new PropertiesBuilder()
                    .name(property.getName())
                    .typeId(property.getTypeId())
                    .price(property.getPrice())
                    .surfaceArea(property.getSurfaceArea())
                    .roomCount(property.getRoomCount())
                    .diagnostic(property.getDiagnostic())
                    .country(property.getCountry())
                    .city(property.getCity())
                    .area(property.getArea())
                    .onSale(property.getOnSale())
                    .build());
        } catch (Exception e) {
            throw new ResponseException(e.getMessage(), 500);
        }
    }

    public Properties updateProperty(int id, Properties property) throws ResourceNotFoundException {
        return propertiesRepository.findById(id).map(existingProperty ->
        {
            if (property.getName() != null) {
                existingProperty.setName(property.getName());
            }
            if (property.getTypeId() != null) {
                existingProperty.setTypeId(property.getTypeId());
            }
            if (property.getPrice() != null) {
                existingProperty.setPrice(property.getPrice());
            }
            if (property.getSurfaceArea() != null) {
                existingProperty.setSurfaceArea(property.getSurfaceArea());
            }
            if (property.getRoomCount() != null) {
                existingProperty.setRoomCount(property.getRoomCount());
            }
            if (property.getDiagnostic() != null) {
                existingProperty.setDiagnostic(property.getDiagnostic());
            }
            if (property.getCountry() != null) {
                existingProperty.setCountry(property.getCountry());
            }
            if (property.getCity() != null) {
                existingProperty.setCity(property.getCity());
            }
            if (property.getArea() != null) {
                existingProperty.setArea(property.getArea());
            }
            if (property.isOnSale() != null) {
                existingProperty.setOnSale(property.isOnSale());
            }
            return propertiesRepository.save(existingProperty);
        }).orElseThrow(() -> new ResourceNotFoundException("Property not found"));
    }

    public Properties deleteProperty(int id) throws ResourceNotFoundException {
        return propertiesRepository.findById(id).map(property -> {
            propertiesRepository.delete(property);
            return property;
        }).orElseThrow(() -> new ResourceNotFoundException("Property not found"));
    }
}
