package org.example.movierentals.server.repository;

import org.example.movierentals.common.domain.Rental;
import org.example.movierentals.common.domain.exceptions.MovieRentalsException;
import org.example.movierentals.common.domain.exceptions.ValidatorException;
import org.example.movierentals.common.domain.validators.RentalValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@org.springframework.stereotype.Repository
public class RentalDBRepository implements Repository<Long, Rental> {
    @Autowired
    private JdbcOperations jdbcOperations;

    RentalValidator validator = new RentalValidator();

    @Override
    public Optional<Rental> findOne(Long id) {
        String query = "SELECT * FROM rentals WHERE id=?";
        try {
            Rental rental = jdbcOperations.queryForObject(query, new Object[]{id}, (resultSet, i) -> {
                Rental rentalResult = new Rental();
                setFieldsOnRental(resultSet, rentalResult);
                return rentalResult;
            });
            return Optional.ofNullable(rental);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new MovieRentalsException(e.getMessage());
        }
    }

    @Override
    public Iterable<Rental> findAll() {
        String query = "SELECT * FROM rentals";
        try {
            return jdbcOperations.query(query, (resultSet, i) -> {
                Rental rental = new Rental();
                setFieldsOnRental(resultSet, rental);
                return rental;
            });
        } catch (DataAccessException | MovieRentalsException e) {
            throw new MovieRentalsException(e.getMessage());
        }
    }

    private void setFieldsOnRental(ResultSet resultSet, Rental rental) {
        try {
            Long resultId = resultSet.getLong("id");
            rental.setId(resultId);

            Long movieId = resultSet.getLong("movie_id");
            rental.setMovieId(movieId);

            Long clientId = resultSet.getLong("client_id");
            rental.setClientId(clientId);

            float rentalCharge = resultSet.getFloat("rental_charge");
            rental.setRentalCharge(rentalCharge);

            Timestamp rentalDate = resultSet.getTimestamp("rental_date");
            rental.setRentalDate(rentalDate.toLocalDateTime());

            Timestamp dueDate = resultSet.getTimestamp("due_date");
            rental.setDueDate(dueDate.toLocalDateTime());

            validator.validate(rental);

        } catch (SQLException | ValidatorException e) {
            throw new MovieRentalsException("Rental is not valid.");
        }
    }

    @Override
    public Optional<Rental> save(Rental rental) {
        try {
            validator.validate(rental);
        } catch (ValidatorException e) {
            throw new ValidatorException(e.getMessage());
        }
        String query = "INSERT INTO rentals (" +
                "movie_id, client_id, rental_charge, rental_date, due_date) " +
                "values (?, ?, ?, ?, ?)";
        try {
            int rowsAffected = jdbcOperations.update(query,
                    rental.getMovieId(),
                    rental.getClientId(),
                    rental.getRentalCharge(),
                    rental.getRentalDate(),
                    rental.getDueDate());
            return rowsAffected > 0 ? Optional.of(rental) : Optional.empty();
        } catch (DataAccessException e) {
            throw new MovieRentalsException(e.getMessage());
        }
    }

    @Override
    public Optional<Rental> delete(Long id) {
        Optional<Rental> optional = findOne(id);
        String query = "DELETE FROM rentals WHERE id = ?";
        try {
            int rowsAffected = jdbcOperations.update(query, id);
            return rowsAffected > 0 ? optional : Optional.empty();
        } catch (DataAccessException e) {
            throw new MovieRentalsException(e.getMessage());
        }
    }

    @Override
    public Optional<Rental> update(Rental rental) {
        try {
            validator.validate(rental);
        } catch (ValidatorException e) {
            throw new ValidatorException(e.getMessage());
        }
        String query = "UPDATE rentals " +
                "SET movie_id=?, client_id=?, rental_charge=?, rental_date=?, due_date=? " +
                "WHERE id= ?";
        try {
            int rowsAffected = jdbcOperations.update(query,
                    rental.getMovieId(),
                    rental.getClientId(),
                    rental.getRentalCharge(),
                    rental.getRentalDate(),
                    rental.getDueDate(),
                    rental.getId());
            return rowsAffected > 0 ? Optional.of(rental) : Optional.empty();
        } catch (DataAccessException e) {
            throw new MovieRentalsException(e.getMessage());
        }
    }
}
