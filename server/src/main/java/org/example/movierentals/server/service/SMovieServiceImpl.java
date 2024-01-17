package org.example.movierentals.server.service;

import org.example.movierentals.common.IMovieService;
import org.example.movierentals.common.domain.Movie;
import org.example.movierentals.common.domain.exceptions.MovieRentalsException;
import org.example.movierentals.server.repository.MovieDBRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SMovieServiceImpl implements IMovieService {
    private final MovieDBRepository movieRepository;

    public SMovieServiceImpl(MovieDBRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Iterable<Movie> getAllMovies() throws MovieRentalsException {
        return movieRepository.findAll();
    }

    @Override
    public void addMovie(Movie movie) throws MovieRentalsException {
        if (movie == null) {
            throw new IllegalArgumentException("Movie must not be null. ");
        }
        Optional<Movie> optional = movieRepository.save(movie);
        if (optional.isEmpty()) {
            throw new MovieRentalsException("Movie was not saved. ");
        }
    }

    @Override
    public Movie getMovieById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null. ");
        }
        Optional<Movie> optional = movieRepository.findOne(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new MovieRentalsException("Movie with ID " + id + " not found.");
        }
    }

    @Override
    public void updateMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie must not be null. ");
        }
        Optional<Movie> optional = movieRepository.update(movie);
        if (optional.isEmpty()) {
            throw new MovieRentalsException("Movie was not updated. ");
        }
    }

    @Override
    public void deleteMovieById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null. ");
        }
        Optional<Movie> optional = movieRepository.delete(id);
        if (optional.isEmpty()) {
            throw new MovieRentalsException("Movie with ID " + id + " not found. ");
        }
    }

    @Override
    public Iterable<Movie> filterMoviesByKeyword(String keyword) throws MovieRentalsException {
        if (keyword == null) {
            throw new IllegalArgumentException("Keyword must not be null. ");
        }
        Iterable<Movie> movies = movieRepository.findAll();
        return StreamSupport.stream(movies.spliterator(), false)
                .filter(movie -> movie.getTitle()
                        .toLowerCase()
                        .contains(keyword.toLowerCase()))
                .collect(Collectors.toSet());
    }
}
