package com.moviedekho.movieservice.service;

import com.moviedekho.movieservice.document.MovieDocument;
import com.moviedekho.movieservice.model.request.MovieRequest;
import com.moviedekho.movieservice.model.response.GenericResponse;
import com.moviedekho.movieservice.model.response.MovieResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<MovieDocument> getAllMovies();

    MovieDocument getMovieById(String id, MovieRequest movie) throws Exception;

    MovieDocument getMovieById(String id);

    MovieDocument createMovie(MovieDocument movie);

    Optional<MovieDocument> updateMovie(String id, MovieDocument updatedMovie);

    GenericResponse deleteMovie(String id) throws Exception;

    MovieResponse addMovie(MovieRequest movie) throws IOException;

    List<MovieDocument> searchByGenre(String genre);

    List<MovieDocument> searchByRating(String rating);

    List<MovieDocument> searchByActor(String actor);

    List<MovieDocument> searchByYearOfRelease(Integer yearOfRelease);


    MovieResponse updateMovie(MovieRequest updateMovieRequest) throws Exception;

    List<MovieDocument> searchByCriteria(String actor, Integer yearOfRelease, String title);

    List<MovieDocument> searchByTitle(String title);

    GenericResponse deleteMovieByTitle(String title);
}