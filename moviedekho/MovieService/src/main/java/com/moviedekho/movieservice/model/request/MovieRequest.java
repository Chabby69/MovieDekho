package com.moviedekho.movieservice.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MovieRequest {


    private String title;
    private String actors;
    private String genre;
    private Integer yearOfRelease;
    private String rating;
    private String streamLink;
    private String moviePoster;
    private MultipartFile videoFile;
    private boolean isFavorited;
    private String username;

}
