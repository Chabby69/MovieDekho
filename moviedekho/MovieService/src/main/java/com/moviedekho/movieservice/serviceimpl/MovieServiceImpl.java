package com.moviedekho.movieservice.serviceimpl;

import com.mongodb.DuplicateKeyException;
import com.moviedekho.movieservice.client.UserServiceClient;
import com.moviedekho.movieservice.document.MovieDocument;
import com.moviedekho.movieservice.exceptions.MovieAlreadyExistsException;
import com.moviedekho.movieservice.model.request.FavoriteMovieEntity;
import com.moviedekho.movieservice.model.request.MovieRequest;
import com.moviedekho.movieservice.model.response.GenericResponse;
import com.moviedekho.movieservice.model.response.MovieResponse;
import com.moviedekho.movieservice.repository.MovieRepository;
import com.moviedekho.movieservice.service.GridFSService;
import com.moviedekho.movieservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private GridFSService gridFSService;

    @Autowired
    private UserServiceClient userServiceClient;


    @Override
    public List<MovieDocument> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public MovieDocument getMovieById(String id, MovieRequest movie) throws Exception {
        Optional<MovieDocument> movieOptional = movieRepository.findById(id);
        MovieDocument existingMovie = movieOptional.get();
        existingMovie.setTitle(movie.getTitle());
        existingMovie.setActors(movie.getActors());
        existingMovie.setGenre(movie.getGenre());
        existingMovie.setYearOfRelease(movie.getYearOfRelease());
        existingMovie.setRating(movie.getRating());
        existingMovie.setStreamLink(movie.getStreamLink());
        existingMovie.setMoviePoster(movie.getMoviePoster());
        movieRepository.save(existingMovie);
        return movieOptional.get();
    }

    @Override
    public MovieDocument getMovieById(String id) {
        Optional<MovieDocument> movieOptional = movieRepository.findById(id);
        return movieOptional.orElseGet(MovieDocument::new);
    }

    @Override
    public MovieDocument createMovie(MovieDocument movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Optional<MovieDocument> updateMovie(String id, MovieDocument updatedMovie) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movie.setTitle(updatedMovie.getTitle());
                    movie.setActors(updatedMovie.getActors());
                    movie.setGenre(updatedMovie.getGenre());
                    movie.setYearOfRelease(updatedMovie.getYearOfRelease());
                    movie.setRating(updatedMovie.getRating());
                    movie.setStreamLink(updatedMovie.getStreamLink());
                    movie.setMoviePoster(updatedMovie.getMoviePoster());
                    return movieRepository.save(movie);
                });
    }

    @Override
    public GenericResponse deleteMovie(String id) throws Exception {
        Optional<MovieDocument> existingMovie = movieRepository.findById(id);
        if (existingMovie.isPresent()) {
            movieRepository.deleteById(id);
            return new GenericResponse("Movie Details Deleted Successfully");
        } else {
            throw new Exception("Unable To Delete Movie Details...");
        }
    }

    @Override
    public MovieResponse addMovie(MovieRequest movieRequest) throws IOException {
        if (!isValidRequest(movieRequest)) {
            throw new MovieAlreadyExistsException("A movie with the given details already exists.");
        }
        isMovieFavorated(movieRequest);
        MovieDocument movieDocument = mapMovieDocument(movieRequest);

        MovieDocument savedDocument;
        try {
            savedDocument = movieRepository.save(movieDocument);
            return getMovieResponse(savedDocument, movieDocument);
        } catch (DuplicateKeyException e) {
            throw new MovieAlreadyExistsException("A movie with the given details already exists.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to save movie: " + e.getMessage(), e);
        }
    }

    private void isMovieFavorated(MovieRequest movieRequest) {

        ResponseEntity<FavoriteMovieEntity> favoriteMovie  = userServiceClient.searchMovieByUsernameAndTitle(movieRequest.getUsername(),
                movieRequest.getTitle());
        if (favoriteMovie.getStatusCode().is2xxSuccessful() && favoriteMovie.getBody() != null) {
            FavoriteMovieEntity favoriteMovieEntity = favoriteMovie.getBody();

            if (favoriteMovieEntity.isFavorited()) {
                movieRequest.setFavorited(true);
            } else {
                movieRequest.setFavorited(false);
            }
        } else {
            movieRequest.setFavorited(false);
        }


    }

    private boolean isValidRequest(MovieRequest movieRequest) {
        List<MovieDocument> movieDetails = movieRepository.findByTitleContainingIgnoreCase(movieRequest.getTitle());
        return movieDetails.isEmpty();
    }

    private MovieResponse getMovieResponse(MovieDocument savedDocument, MovieDocument movieDocument) {
        MovieResponse movieResponse = new MovieResponse("Movie Details Added SuccessFully");
        movieResponse.setTitle(savedDocument.getTitle());
        movieResponse.setActors(savedDocument.getActors());
        movieResponse.setRating(savedDocument.getRating());
        movieResponse.setGenre(movieDocument.getGenre());
        movieResponse.setYearOfRelease(movieDocument.getYearOfRelease());
        movieResponse.setStreamLink(movieDocument.getStreamLink());
        movieResponse.setPosterLink(savedDocument.getMoviePoster());
        movieResponse.setVideoFileId(savedDocument.getVideoFileId());
        return movieResponse;
    }

    private MovieDocument mapMovieDocument(MovieRequest movieRequest) throws IOException {

        MovieDocument movieDocument = new MovieDocument();
        movieDocument.setTitle(movieRequest.getTitle());
        movieDocument.setActors(movieRequest.getActors());
        movieDocument.setGenre(movieRequest.getGenre());
        movieDocument.setYearOfRelease(movieRequest.getYearOfRelease());
        movieDocument.setRating(movieRequest.getRating());
        movieDocument.setStreamLink(movieRequest.getStreamLink());
        movieDocument.setMoviePoster(movieRequest.getMoviePoster());
        movieDocument.setFavorited(movieRequest.isFavorited());
        // Store the MP4 file and get its GridFS ID
        String videoFileId = gridFSService.storeFile(movieRequest.getVideoFile());
        movieDocument.setVideoFileId(videoFileId);

        return movieDocument;
    }


    @Override
    public List<MovieDocument> searchByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }

    @Override
    public List<MovieDocument> searchByRating(String rating) {
        return movieRepository.findByRating(rating);
    }

    @Override
    public List<MovieDocument> searchByActor(String actor) {
        return movieRepository.findByActorsContaining(actor);
    }

    @Override
    public List<MovieDocument> searchByYearOfRelease(Integer yearOfRelease) {
        return movieRepository.findByYearOfRelease(yearOfRelease);
    }

    @Override
    public List<MovieDocument> searchByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public MovieResponse updateMovie(MovieRequest updateMovieRequest) throws Exception {

        List<MovieDocument> movieDetails = movieRepository.findByTitleContainingIgnoreCase(updateMovieRequest.getTitle());

        if (movieDetails.isEmpty()) {
            throw new Exception("Movie Not Found");
        }
        MovieDocument movie = movieDetails.get(0);

        movie.setActors(updateMovieRequest.getActors() != null ? updateMovieRequest.getActors() : movie.getActors());
        movie.setGenre(updateMovieRequest.getGenre() != null ? updateMovieRequest.getGenre() : movie.getGenre());
        movie.setYearOfRelease(updateMovieRequest.getYearOfRelease() != null ? updateMovieRequest.getYearOfRelease() : movie.getYearOfRelease());
        movie.setRating(updateMovieRequest.getRating() != null ? updateMovieRequest.getRating() : movie.getRating());
        movie.setStreamLink(updateMovieRequest.getStreamLink() != null ? updateMovieRequest.getStreamLink() : movie.getStreamLink());
        movie.setMoviePoster(updateMovieRequest.getMoviePoster() != null ? updateMovieRequest.getMoviePoster() : movie.getMoviePoster());
        if (updateMovieRequest.getVideoFile() != null) {
            String videoFileId = gridFSService.storeFile(updateMovieRequest.getVideoFile());
            movie.setVideoFileId(updateMovieRequest.getVideoFile() != null ? videoFileId : movie.getVideoFileId());
        }
        movieRepository.save(movie);

        return getMovieResponse(movie);
    }

    private static MovieResponse getMovieResponse(MovieDocument movie) {
        MovieResponse movieResponse = new MovieResponse("Movie updated successfully.");
        movieResponse.setTitle(movie.getTitle());
        movieResponse.setActors(movie.getActors());
        movieResponse.setGenre(movie.getGenre());
        movieResponse.setYearOfRelease(movie.getYearOfRelease());
        movieResponse.setRating(movie.getRating());
        movieResponse.setStreamLink(movie.getStreamLink());
        movieResponse.setPosterLink(movie.getMoviePoster());
        movieResponse.setVideoFileId(movie.getVideoFileId());
        return movieResponse;
    }

    public List<MovieDocument> searchByCriteria(String actor,
                                                Integer yearOfRelease,
                                                String title) {
        List<MovieDocument> movies = new ArrayList<>();
        Query query = new Query();

        if (actor != null && !actor.isEmpty()) {
            query.addCriteria(Criteria.where("actors").regex(".*" + actor + ".*", "i"));
        }

        if (yearOfRelease != null) {
            query.addCriteria(Criteria.where("yearOfRelease").is(yearOfRelease));
        }

        if (title != null && !title.isEmpty()) {
            query.addCriteria(Criteria.where("title").regex(".*" + title + ".*", "i"));
        }

        return mongoTemplate.find(query, MovieDocument.class);
    }

    @Override
    public GenericResponse deleteMovieByTitle(String title) {
        List<MovieDocument> movieDetails = movieRepository.findByTitleContainingIgnoreCase(title);
        if (movieDetails == null || movieDetails.isEmpty()) {
            return new GenericResponse("Movie not found");
        } else {
            movieRepository.deleteAll(movieDetails);
            return new GenericResponse("Movies deleted successfully");
        }
    }
}
