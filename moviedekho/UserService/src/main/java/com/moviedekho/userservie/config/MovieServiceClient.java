package com.moviedekho.userservie.config;


import com.moviedekho.userservie.model.response.MovieDocument;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "movieservice", url = "http://localhost:8082/api/movie")
public interface MovieServiceClient {

    @GetMapping("/searchMovieByTitle")
    ResponseEntity<List<MovieDocument>> searchMovieByTitle(@RequestParam("title") String title);

}
