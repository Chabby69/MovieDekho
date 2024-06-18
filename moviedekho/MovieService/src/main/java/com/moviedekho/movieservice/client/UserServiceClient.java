package com.moviedekho.movieservice.client;

import com.moviedekho.movieservice.model.request.UserDetail;
import com.moviedekho.movieservice.model.request.FavoriteMovieEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "userservice", url = "http://localhost:8081")
public interface UserServiceClient {

    @GetMapping("/api/auth/users/{username}/details")
    UserDetail getUserDetails(@PathVariable("username") String username);

    @GetMapping("/api/auth/getFavoriteMovieByTitle/{username}/{title}")
    ResponseEntity<FavoriteMovieEntity> searchMovieByUsernameAndTitle(@PathVariable("username") String username,
                                                                      @PathVariable("title") String title);

}
