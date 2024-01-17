package org.example.movierentals.service;

import org.example.movierentals.common.IMovieService;
import org.example.movierentals.common.domain.Movie;
import org.example.movierentals.common.domain.exceptions.MovieRentalsException;
import org.springframework.beans.factory.annotation.Autowired;

public class CMovieServiceImpl implements IMovieService {

    @Autowired
    private IMovieService movieService;

    @Override
    public Iterable<Movie> getAllMovies() throws MovieRentalsException {
        return movieService.getAllMovies();
    }

    @Override
    public void addMovie(Movie movie) throws MovieRentalsException {
        movieService.addMovie(movie);
    }

    @Override
    public Movie getMovieById(Long id) throws MovieRentalsException {
        return movieService.getMovieById(id);
    }

    @Override
    public void updateMovie(Movie movie) throws MovieRentalsException {
        movieService.updateMovie(movie);
    }

    @Override
    public void deleteMovieById(Long id) throws MovieRentalsException {
        movieService.deleteMovieById(id);
    }

    @Override
    public Iterable<Movie> filterMoviesByKeyword(String keyword) throws MovieRentalsException {
        return movieService.filterMoviesByKeyword(keyword);
    }
}