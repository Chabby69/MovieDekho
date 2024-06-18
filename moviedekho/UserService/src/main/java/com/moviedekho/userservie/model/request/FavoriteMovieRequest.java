package com.moviedekho.userservie.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteMovieRequest {

    private String movieTitle;
    private String username;
    private boolean isFavorited;
}
