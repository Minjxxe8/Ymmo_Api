package com.ymmo.ymmoapi.repository;

import com.ymmo.ymmoapi.model.Properties;
import com.ymmo.ymmoapi.model.PropertyPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyPictureRepository extends JpaRepository<PropertyPicture, Integer> {
    List<PropertyPicture> findAllByPropertyId(int propertyId);

    void deleteByPropertyAndPath(Properties property, String path);

    @Query("SELECT path FROM property_picture WHERE property.id = :#{#property.id}")
    List<String> getAllPathsByProperty(@Param("property") Properties property);

    @Query("SELECT path FROM property_picture WHERE property.id = :#{#property.id} ORDER BY id ASC LIMIT 1")
    String getFirstByProperty(@Param("property") Properties property);
}
