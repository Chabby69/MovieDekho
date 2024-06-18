package com.moviedekho.userservie.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FavoriteMovieResponse {

    private List<String> favoriteMovies;
    private String username;
    private String message;
    private List<MovieDocument> movieDocumentList;
}
