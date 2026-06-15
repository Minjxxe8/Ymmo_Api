package com.ymmo.ymmoapi.repository;

import com.ymmo.ymmoapi.model.Properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertiesRepository extends JpaRepository<Properties, Integer> {
    Optional<Properties> findById(int id);

    boolean existsPropertiesById(int id);

    @Query("SELECT p FROM properties p WHERE " +
            "LOWER(p.name) LIKE %:query% OR " +
            "LOWER(p.city) LIKE %:query% OR " +
            "LOWER(p.area) LIKE %:query%")
    Optional<List<Properties>> searchAllFields(@Param("query") String query);
}
