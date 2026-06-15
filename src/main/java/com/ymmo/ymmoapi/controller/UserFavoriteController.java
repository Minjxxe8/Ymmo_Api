package com.ymmo.ymmoapi.controller;

import com.ymmo.ymmoapi.dto.FavoritesDto;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserFavoriteController {
    private final FavoriteService favoriteService;

    public UserFavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/me/favorites")
    public ResponseEntity<?> getAllMyFavorites() {
        try {
            String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                            .getAuthentication())
                    .getName();
            return ResponseEntity.ok(favoriteService.getAllUserFavorites(email));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @PostMapping("/me/favorites")
    public ResponseEntity<?> addToMyFavorites(@RequestBody FavoritesDto.Favorite favorite) {
        try {
            String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                            .getAuthentication())
                    .getName();
            return ResponseEntity.ok(favoriteService.addUserFavorite(email, favorite.id()));
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }

    @DeleteMapping("/me/favorites/{id}")
    public ResponseEntity<?> deleteToMyFavorites(@PathVariable int id) {
        try {
            String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                            .getAuthentication())
                    .getName();
            favoriteService.deleteUserFavorite(email, id);
            return ResponseEntity.ok("Favorite deleted");
        } catch (ResponseException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        }
    }
}
