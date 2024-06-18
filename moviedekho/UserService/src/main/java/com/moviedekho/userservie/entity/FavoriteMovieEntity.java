package com.moviedekho.userservie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "favorite_movie")
public class FavoriteMovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "favoriteMovie")
    private String favoriteMovie;

    @Column(name = "isFavorited")
    private boolean isFavorited;
}
