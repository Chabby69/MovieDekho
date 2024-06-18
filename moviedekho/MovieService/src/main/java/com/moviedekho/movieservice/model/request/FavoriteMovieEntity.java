package com.moviedekho.movieservice.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FavoriteMovieEntity {


    private String title;
    private boolean isFavorited;
    private String username;

}
