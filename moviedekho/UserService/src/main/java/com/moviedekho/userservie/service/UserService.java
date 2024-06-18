package com.moviedekho.userservie.service;


import com.moviedekho.userservie.entity.FavoriteMovieEntity;
import com.moviedekho.userservie.model.request.FavoriteMovieRequest;
import com.moviedekho.userservie.model.request.UserRequest;
import com.moviedekho.userservie.model.response.FavoriteMovieResponse;
import com.moviedekho.userservie.model.response.MovieDocument;
import com.moviedekho.userservie.model.response.UserLoginResponse;
import com.moviedekho.userservie.model.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    public UserResponse register(UserRequest userRequest) throws Exception;

    public UserLoginResponse authenticate(String username, String password);

    UserLoginResponse getUserDetails(String username);

    UserLoginResponse updateUserDetails(UserRequest user) throws Exception;

    FavoriteMovieResponse addFavoriteMovie(FavoriteMovieRequest userFavoriteMovie) throws Exception;

    ResponseEntity<List<MovieDocument>> getFavoriteMovies(String username);

    FavoriteMovieResponse removeFavoriteMovie(String username, String title) throws Exception;

    ResponseEntity<FavoriteMovieEntity> getFavoriteMovieByTitle(String username, String title);
}
