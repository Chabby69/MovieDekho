package com.moviedekho.userservie.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDocument {


    private String title;
    private String actors;
    private String genre;
    private Integer yearOfRelease;
    private String rating;
    private String streamLink;
    private String moviePoster;
    private String videoFileId;
    private String isFavorited;
}
