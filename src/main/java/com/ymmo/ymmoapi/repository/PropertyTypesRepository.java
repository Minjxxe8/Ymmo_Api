package com.ymmo.ymmoapi.repository;

import com.ymmo.ymmoapi.model.PropertyTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyTypesRepository extends JpaRepository<PropertyTypes, Integer> {
    Optional<PropertyTypes> findById(int id);
    Optional<List<PropertyTypes>> findByName(String name);
}
