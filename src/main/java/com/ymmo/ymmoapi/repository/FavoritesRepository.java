package com.ymmo.ymmoapi.repository;

import com.ymmo.ymmoapi.model.Favorites;
import com.ymmo.ymmoapi.model.Properties;
import com.ymmo.ymmoapi.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {
    Optional<List<Favorites>> getFavoritesByUser(Users user);

    boolean existsFavoritesByUserAndProperty(Users user, Properties property);

    @Modifying
    @Query("DELETE FROM favorites WHERE user.id = :#{#user.id} AND property.id = :#{#property.id}")
    void deleteFavoritesByUserAndProperty(@Param("user") Users user, @Param("property") Properties property);
}
