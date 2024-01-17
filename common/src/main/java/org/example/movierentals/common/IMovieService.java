package org.example.movierentals.common;

import org.example.movierentals.common.domain.Movie;

public interface IMovieService {

    Iterable<Movie> getAllMovies();

    void addMovie(Movie movie);

    Movie getMovieById(Long id);

    void updateMovie(Movie movie);

    void deleteMovieById(Long id);

    Iterable<Movie> filterMoviesByKeyword(String keyword);
}
