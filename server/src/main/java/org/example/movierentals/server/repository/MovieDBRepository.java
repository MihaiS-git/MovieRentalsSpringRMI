package org.example.movierentals.server.repository;

import org.example.movierentals.common.domain.AgeRestrictions;
import org.example.movierentals.common.domain.Movie;
import org.example.movierentals.common.domain.MovieGenres;
import org.example.movierentals.common.domain.exceptions.MovieRentalsException;
import org.example.movierentals.common.domain.exceptions.ValidatorException;

import org.example.movierentals.common.domain.validators.MovieValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@org.springframework.stereotype.Repository
public class MovieDBRepository implements Repository<Long, Movie> {
    @Autowired
    private JdbcOperations jdbcOperations;

    MovieValidator validator = new MovieValidator();

    @Override
    public Optional<Movie> findOne(Long id) {
        String query = "SELECT * FROM movies WHERE id=?";
        try {
            List<Movie> movies = jdbcOperations.query(query, new Object[]{id}, (resultSet, i) -> {
                Movie movieResult = new Movie();
                setFieldsOnMovie(resultSet, movieResult);
                return movieResult;
            });
            return movies.isEmpty() ? Optional.empty() : Optional.of(movies.get(0));
        } catch (DataAccessException e) {
            throw new MovieRentalsException(e.getMessage());
        }
    }

    @Override
    public Iterable<Movie> findAll() {
        String query = "SELECT * FROM movies";
        try {
            return jdbcOperations.query(query, (resultSet, i) -> {
                Movie movie = new Movie();
                setFieldsOnMovie(resultSet, movie);
                return movie;
            });
        } catch (DataAccessException | ValidatorException e) {
            throw new MovieRentalsException(e.getMessage());
        }
    }

    private void setFieldsOnMovie(ResultSet resultSet, Movie movie) throws SQLException {
        Long resultId = resultSet.getLong("id");
        movie.setId(resultId);

        String title = resultSet.getString("title");
        movie.setTitle(title);

        int year = resultSet.getInt("year");
        movie.setYear(year);

        MovieGenres genre = MovieGenres.valueOf(resultSet.getString("genre").toUpperCase());
        movie.setGenre(genre);

        AgeRestrictions ageRestriction = AgeRestrictions.valueOf(resultSet.getString("age_restriction").toUpperCase());
        movie.setAgeRestrictions(ageRestriction);

        float rentalPrice = resultSet.getFloat("rental_price");
        movie.setRentalPrice(rentalPrice);

        boolean available = resultSet.getBoolean("available");
        movie.setAvailable(available);

        try {
            validator.validate(movie);
        } catch (ValidatorException e) {
            throw new ValidatorException("Movie is not valid.");
        }
    }

    @Override
    public Optional<Movie> save(Movie movie) {
        try {
            validator.validate(movie);
        } catch (ValidatorException e) {
            throw new MovieRentalsException(e.getMessage());
        }
        String query = "INSERT INTO movies (" +
                "title, year, genre, age_restriction, rental_price, available) " +
                "values (?, ?, ?, ?, ?, ?)";
        try {
            int rowsAffected = jdbcOperations.update(query,
                    movie.getTitle(),
                    movie.getYear(),
                    String.valueOf(movie.getGenre()),
                    String.valueOf(movie.getAgeRestrictions()),
                    movie.getRentalPrice(),
                    movie.isAvailable());
            return rowsAffected > 0 ? Optional.of(movie) : Optional.empty();
        } catch (DataAccessException e) {
            throw new MovieRentalsException(e.getMessage());
        }
    }

    @Override
    public Optional<Movie> delete(Long id) {
        Optional<Movie> optional = findOne(id);
        if(optional.isPresent()) {
            String query = "DELETE FROM movies WHERE id = ?";
            try {
                int rowsAffected = jdbcOperations.update(query, id);
                return rowsAffected > 0 ? optional : Optional.empty();
            } catch (DataAccessException e) {
                throw new MovieRentalsException(e.getMessage());
            }
        } else {
            throw new MovieRentalsException("Movie not found. ");
        }
    }

    @Override
    public Optional<Movie> update(Movie movie) {
        try {
            validator.validate(movie);
        } catch (ValidatorException e) {
            throw new MovieRentalsException(e.getMessage());
        }
        String query = "UPDATE movies " +
                "SET title = ?, year = ?, genre = ?, age_restriction = ?, " +
                "rental_price = ?, available = ?" +
                "WHERE id = ?";
        try {
            int rowsAffected = jdbcOperations.update(query,
                    movie.getTitle(),
                    movie.getYear(),
                    String.valueOf(movie.getGenre()),
                    String.valueOf(movie.getAgeRestrictions()),
                    movie.getRentalPrice(),
                    movie.isAvailable(),
                    movie.getIdMovie());
            return rowsAffected > 0 ? Optional.of(movie) : Optional.empty();
        } catch (DataAccessException e) {
            throw new MovieRentalsException(e.getMessage());
        }
    }
}
