package com.moviedekho.movieservice.controller;

import com.moviedekho.movieservice.document.MovieDocument;
import com.moviedekho.movieservice.model.request.MovieRequest;
import com.moviedekho.movieservice.model.response.GenericResponse;
import com.moviedekho.movieservice.model.response.MovieResponse;
import com.moviedekho.movieservice.service.GridFSService;
import com.moviedekho.movieservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/movie")
@CrossOrigin(origins = {"http://localhost:4200", "*"})
public class MovieController {

    private final MovieService movieService;

    @Autowired
    private GridFSService gridFSService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/getAllMovies")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<MovieDocument>> getAllMovies() {
        List<MovieDocument> movies = movieService.getAllMovies();
        ;
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }


    @PostMapping("/addMovieDetails")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createMovie(@RequestParam("title") String title,
                                         @RequestParam("actors") String actors,
                                         @RequestParam("genre") String genre,
                                         @RequestParam("yearOfRelease") Integer yearOfRelease,
                                         @RequestParam("rating") String rating,
                                         @RequestParam("streamLink") String streamLink,
                                         @RequestParam("moviePoster") String moviePoster,
                                         @RequestParam("videoFile") MultipartFile videoFile)
            throws IOException {
        if (yearOfRelease == null || yearOfRelease < 1900 || yearOfRelease > 2100) {
            return ResponseEntity.badRequest().body("Invalid year of release.");
        }
        MovieRequest movie = new MovieRequest();
        movie.setYearOfRelease(yearOfRelease);
        movie.setMoviePoster(moviePoster);
        movie.setRating(rating);
        movie.setGenre(genre);
        movie.setActors(actors);
        movie.setTitle(title);
        movie.setStreamLink(streamLink);
        movie.setVideoFile(videoFile);
        MovieResponse savedMovie = movieService.addMovie(movie);
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
    }

    @PatchMapping("/updateMovieDetails")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateMovie(@RequestParam("title") String title,
                                         @RequestParam(value = "actors", required = false) String actors,
                                         @RequestParam(value = "genre", required = false) String genre,
                                         @RequestParam(value = "yearOfRelease", required = false) Integer yearOfRelease,
                                         @RequestParam(value = "rating", required = false) String rating,
                                         @RequestParam(value = "streamLink", required = false) String streamLink,
                                         @RequestParam(value = "moviePoster", required = false) String moviePoster,
                                         @RequestParam(value = "videoFile", required = false) MultipartFile videoFile) throws Exception {
        if (yearOfRelease == null || yearOfRelease < 1900 || yearOfRelease > 2100) {
            return ResponseEntity.badRequest().body("Invalid year of release.");
        }
        MovieRequest movie = new MovieRequest();
        movie.setYearOfRelease(yearOfRelease);
        movie.setMoviePoster(moviePoster);
        movie.setRating(rating);
        movie.setGenre(genre);
        movie.setActors(actors);
        movie.setTitle(title);
        movie.setStreamLink(streamLink);
        movie.setVideoFile(videoFile);

        MovieResponse updatedMovieDetails = movieService.updateMovie(movie);
        return ResponseEntity.ok(updatedMovieDetails);

    }

    @GetMapping("/video/{fileId}")
    public ResponseEntity<InputStreamResource> getVideoFile(@PathVariable("fileId") String fileId) throws IOException {
        GridFsResource resource = gridFSService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resource.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + resource.getFilename())
                .body(new InputStreamResource(resource.getInputStream()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GenericResponse> deleteMovie(@PathVariable String id) throws Exception {

        GenericResponse genericResponse = movieService.deleteMovie(id);
        return ResponseEntity.ok(genericResponse);


    }

    @DeleteMapping("/deleteMovieByTitle/{title}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GenericResponse> deleteMovieByTitle(@PathVariable("title") String title) throws Exception {

        GenericResponse genericResponse = movieService.deleteMovieByTitle(title);
        return ResponseEntity.ok(genericResponse);


    }

    @GetMapping("/searchMovies")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<MovieDocument>> searchMovies(
            @RequestParam(value = "actor", required = false) String actor,
            @RequestParam(value = "yearOfRelease", required = false) Integer yearOfRelease,
            @RequestParam(value = "title", required = false) String title
    ) {

        List<MovieDocument> movies = movieService.searchByCriteria(actor, yearOfRelease, title);
        if (movies == null || movies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/searchMovieByTitle")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<MovieDocument>> searchMovieByTitle(@RequestParam(value = "title", required = true) String title) {

        List<MovieDocument> movies = movieService.searchByTitle(title);
        if (movies == null || movies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(movies);
    }

}
