package com.moviedekho.userservie.controller;


import com.moviedekho.userservie.entity.FavoriteMovieEntity;
import com.moviedekho.userservie.model.request.FavoriteMovieRequest;
import com.moviedekho.userservie.model.request.UserLoginRequest;
import com.moviedekho.userservie.model.request.UserRequest;
import com.moviedekho.userservie.model.response.FavoriteMovieResponse;
import com.moviedekho.userservie.model.response.MovieDocument;
import com.moviedekho.userservie.model.response.UserLoginResponse;
import com.moviedekho.userservie.model.response.UserResponse;
import com.moviedekho.userservie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200"})
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) throws Exception {
        UserResponse userResponse = userService.register(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserLoginRequest user) {
        UserLoginResponse userLoginResponse = userService.authenticate(user.getUserName(), user.getPassword());
        return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
    }

    @GetMapping("/users/{username}/details")
    private UserLoginResponse getUserDetails(@PathVariable("username") String username) {
        return userService.getUserDetails(username);
    }

    @PatchMapping("/updateUserDetails")
    private UserLoginResponse updateUserDetails(@RequestBody UserRequest user) throws Exception {
        return userService.updateUserDetails(user);
    }

    @PostMapping("/addUserFavoriteMovie")
    private FavoriteMovieResponse addFavoriteMovies(@RequestBody FavoriteMovieRequest userFavoriteMovie) throws Exception {
        return userService.addFavoriteMovie(userFavoriteMovie);
    }

    @DeleteMapping("/removeFavoriteMovie")
    private FavoriteMovieResponse deleteFavoriteMovies(@RequestParam String username, @RequestParam String title) throws Exception {
        return userService.removeFavoriteMovie(username, title);
    }

    @GetMapping("/getFavoriteMovies/{username}")
    private ResponseEntity<List<MovieDocument>> getFavoriteMovies(@PathVariable("username") String username) throws Exception {
        return userService.getFavoriteMovies(username);
    }

    @GetMapping("/getFavoriteMovieByTitle/{username}/{title}")
    private ResponseEntity<FavoriteMovieEntity> getFavoriteMovieByTitle(@PathVariable("username") String username
    , @PathVariable("title") String title) throws Exception {
        return userService.getFavoriteMovieByTitle(username, title);
    }
}
