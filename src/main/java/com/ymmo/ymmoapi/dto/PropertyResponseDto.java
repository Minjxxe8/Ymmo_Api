package com.ymmo.ymmoapi.dto;

import com.ymmo.ymmoapi.model.PropertyTypes;

import java.util.List;

public class PropertyResponseDto {
    public record PropertyFullPictureResponse(
            int id,
            String name,
            PropertyTypes type,
            Double price,
            Double surfaceArea,
            Integer roomCount,
            String diagnostic,
            String country,
            String city,
            String area,
            Boolean onSale,
            List<String> picturePaths
    ) {

    }

    public record PropertyPartialPictureResponse(
            int id,
            String name,
            PropertyTypes type,
            Double price,
            Double surfaceArea,
            Integer roomCount,
            String diagnostic,
            String country,
            String city,
            String area,
            Boolean onSale,
            String picturePath
    ) {

    }
}
