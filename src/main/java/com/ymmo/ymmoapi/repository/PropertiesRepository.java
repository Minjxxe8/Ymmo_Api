package com.ymmo.ymmoapi.repository;

import com.ymmo.ymmoapi.model.Properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertiesRepository extends JpaRepository<Properties, Integer> {
    Optional<Properties> findById(int id);

    boolean existsPropertiesById(int id);

    Optional<Properties> getByName(String name);
}
