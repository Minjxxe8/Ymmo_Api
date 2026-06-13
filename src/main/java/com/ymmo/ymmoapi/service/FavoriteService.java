package com.ymmo.ymmoapi.service;

import com.ymmo.ymmoapi.exception.ResourceNotFoundException;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.model.Favorites;
import com.ymmo.ymmoapi.model.Properties;
import com.ymmo.ymmoapi.model.Users;
import com.ymmo.ymmoapi.repository.FavoritesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {
    private final FavoritesRepository favoritesRepository;
    private final UserService userService;
    private final PropertyService propertyService;

    public FavoriteService(FavoritesRepository favoritesRepository, UserService userService, PropertyService propertyService) {
        this.favoritesRepository = favoritesRepository;
        this.userService = userService;
        this.propertyService = propertyService;
    }

    public List<Favorites> getAllUserFavorites(String email) {
        return favoritesRepository.getFavoritesByUser(userService.getUserByEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("No favorites found for this user"));
    }

    public Favorites addUserFavorite(String email, int propertyId) {
        try {
            Users user = userService.getUserByEmail(email);
            Properties property = propertyService.getPropertyById(propertyId);
            if (favoritesRepository.existsFavoritesByUserAndProperty(user, property)) {
                throw new ResourceNotFoundException("Favorite already exists");
            }
            return favoritesRepository.save(new Favorites(
                    user,
                    property
            ));
        } catch (ResponseException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseException("Error while saving a favorite with for the user: " + email + " for the property with the id: " + propertyId, 500);
        }
    }

    @Transactional
    public void deleteUserFavorite(String email, int propertyId) {
        Users user = userService.getUserByEmail(email);
        Properties property = propertyService.getPropertyById(propertyId);

        if (!favoritesRepository.existsFavoritesByUserAndProperty(user, property)) {
            throw new ResponseException("Favorite not found for user: " + email + " and property id: " + propertyId, 404);
        }

        favoritesRepository.deleteFavoritesByUserAndProperty(user, property);
    }
}
