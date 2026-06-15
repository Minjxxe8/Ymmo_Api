package com.ymmo.ymmoapi.service;

import com.ymmo.ymmoapi.dto.PropertyCreationDto;
import com.ymmo.ymmoapi.dto.PropertyResponseDto;
import com.ymmo.ymmoapi.exception.ResourceNotFoundException;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.model.Properties;
import com.ymmo.ymmoapi.model.PropertiesBuilder;
import com.ymmo.ymmoapi.model.PropertyPicture;
import com.ymmo.ymmoapi.model.PropertyTypes;
import com.ymmo.ymmoapi.repository.PropertiesRepository;
import com.ymmo.ymmoapi.repository.PropertyPictureRepository;
import com.ymmo.ymmoapi.repository.PropertyTypesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Component
public class PropertyService {
    private final PropertiesRepository propertiesRepository;
    private final PropertyTypesRepository propertyTypesRepository;
    private final S3Service s3Service;
    private final PropertyPictureRepository propertyPictureRepository;

    @Autowired
    public PropertyService(PropertiesRepository propertiesRepository, PropertyTypesRepository propertyTypesRepository, S3Service s3Service, PropertyPictureRepository propertyPictureRepository) {
        this.propertiesRepository = propertiesRepository;
        this.propertyTypesRepository = propertyTypesRepository;
        this.s3Service = s3Service;
        this.propertyPictureRepository = propertyPictureRepository;
    }

    public List<PropertyResponseDto.PropertyPartialPictureResponse> getAllProperties() throws ResourceNotFoundException {
        List<Properties> propertiesList = propertiesRepository.findAll();
        List<PropertyResponseDto.PropertyPartialPictureResponse> propertyPartialPictureResponses = propertiesList
                .stream().map(p -> new PropertyResponseDto.PropertyPartialPictureResponse(
                        p.getId(),
                        p.getName(),
                        p.getType(),
                        p.getPrice(),
                        p.getSurfaceArea(),
                        p.getRoomCount(),
                        p.getDiagnostic(),
                        p.getCountry(),
                        p.getCity(),
                        p.getArea(),
                        p.isOnSale(),
                        getFirstPropertyPicture(p)
                )).toList();
        if (propertiesList.isEmpty()) {
            throw new ResourceNotFoundException("No properties have been created");
        }
        return propertyPartialPictureResponses;
    }

    public Properties getPropertyById(int id) {
        return propertiesRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Property with the id " + id + " has not been found"))
                .getBody();
    }

    public PropertyResponseDto.PropertyFullPictureResponse getPropertyResponseById(int id) throws ResourceNotFoundException {
        return propertiesRepository.findById(id)
                .map(p -> new PropertyResponseDto.PropertyFullPictureResponse(
                        p.getId(),
                        p.getName(),
                        p.getType(),
                        p.getPrice(),
                        p.getSurfaceArea(),
                        p.getRoomCount(),
                        p.getDiagnostic(),
                        p.getCountry(),
                        p.getCity(),
                        p.getArea(),
                        p.isOnSale(),
                        getAllPropertyPictures(p)
                ))
                .orElseThrow(() -> new ResourceNotFoundException("Property with the id " + id + " has not been found"));
    }

    public Properties createProperty(PropertyCreationDto property) throws ResponseException {
        try {
            return propertiesRepository.save(new PropertiesBuilder()
                    .name(property.getName())
                    .type(propertyTypesRepository.findById(property.getTypeId())
                            .orElseThrow(() -> new ResourceNotFoundException("Property type not found with the id : " + property.getTypeId())))
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

    public void postPropertyPictures(int property_id, List<MultipartFile> pictures) {
        try {
            for (MultipartFile picture : pictures) {
                String path = s3Service.uploadFile(picture);
                propertyPictureRepository.save(new PropertyPicture(
                        path,
                        propertiesRepository.getReferenceById(property_id)
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public void deletePicture(int id, String path) throws IOException {
        propertyPictureRepository.deleteByPropertyAndPath(propertiesRepository.getReferenceById(id), path);
        s3Service.deleteFile(path);
    }

    public List<String> getAllPropertyPictures(Properties property) {
        return propertyPictureRepository.getAllPathsByProperty(property);
    }

    public String getFirstPropertyPicture(Properties property) {
        return propertyPictureRepository.getFirstByProperty(property);
    }

    public Properties updateProperty(int id, PropertyCreationDto property) throws ResourceNotFoundException {
        return propertiesRepository.findById(id).map(existingProperty ->
        {
            if (property.getName() != null) {
                existingProperty.setName(property.getName());
            }
            if (property.getTypeId() != null) {
                existingProperty.setType(propertyTypesRepository.findById(property.getTypeId())
                        .orElseThrow(() -> new ResourceNotFoundException("No Property types have been found with the id: " + property.getTypeId())));
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
            if (property.getOnSale() != null) {
                existingProperty.setOnSale(property.getOnSale());
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

    public List<PropertyTypes> getAllPropertiesType() throws ResourceNotFoundException {
        List<PropertyTypes> propertyTypes = propertyTypesRepository.findAll();
        if (propertyTypes.isEmpty()) {
            throw new ResourceNotFoundException("No property types have been found");
        }
        return propertyTypes;
    }

    public Properties changePropertyStatus(int id, boolean newStatus) throws ResourceNotFoundException {
        return propertiesRepository.findById(id).map(property -> {
            property.setOnSale(newStatus);
            return propertiesRepository.save(property);
        }).orElseThrow(() -> new ResourceNotFoundException("No property have been found with the id " + id));
    }

    public Set<Properties> search(String query, Integer minPrice, Integer maxPrice) {
        Set<Properties> result = new HashSet<>();

        propertiesRepository.searchAllFields(query.strip().toLowerCase()).ifPresent(result::addAll);

        if (minPrice != null || maxPrice != null) {
            result = result.stream()
                    .filter(Objects::nonNull)
                    .filter(property -> {
                        if (maxPrice == null) {
                            return property.getPrice() > minPrice;
                        }
                        if (minPrice == null) {
                            return property.getPrice() < maxPrice;
                        }
                        return property.getPrice() < maxPrice && property.getPrice() > minPrice;
                    }).collect(Collectors.toSet());
        }

        if (result.isEmpty()) {
            throw new ResourceNotFoundException("No properties have been found");
        }
        return result;
    }
}
