package com.moviedekho.movieservice.repository;

import com.moviedekho.movieservice.document.MovieDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends MongoRepository<MovieDocument, String> {


    List<MovieDocument> findByGenre(String genre);

    List<MovieDocument> findByRating(String rating);

    List<MovieDocument> findByActorsContaining(String actor);

    List<MovieDocument> findByYearOfRelease(Integer yearOfRelease);

    List<MovieDocument> findByTitleContainingIgnoreCase(String title);
}
