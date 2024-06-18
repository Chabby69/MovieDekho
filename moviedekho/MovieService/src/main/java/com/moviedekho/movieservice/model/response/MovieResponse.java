package com.moviedekho.movieservice.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieResponse extends GenericResponse {


    public MovieResponse(String message) {
        super(message);
    }

    private String title;
    private String actors;
    private String genre;
    private Integer yearOfRelease;
    private String rating;
    private String streamLink;
    private String posterLink;
    private String videoFileId;
}
