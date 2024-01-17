package org.example.movierentals.service;

import org.example.movierentals.common.IRentalService;
import org.example.movierentals.common.domain.*;
import org.example.movierentals.common.domain.exceptions.MovieRentalsException;
import org.springframework.beans.factory.annotation.Autowired;

public class CRentalServiceImpl implements IRentalService {

    @Autowired
    private IRentalService rentalService;

    @Override
    public Iterable<Rental> getAllRentals() throws MovieRentalsException {
        return rentalService.getAllRentals();
    }

    @Override
    public Rental getRentalById(Long id) throws MovieRentalsException {
        return rentalService.getRentalById(id);
    }

    @Override
    public void rentAMovie(Rental rental) throws MovieRentalsException {
        rentalService.rentAMovie(rental);
    }

    @Override
    public void updateRentalTransaction(Rental rental) throws MovieRentalsException {
        rentalService.updateRentalTransaction(rental);
    }

    @Override
    public void deleteMovieRental(Long rentalId) throws MovieRentalsException {
        rentalService.deleteMovieRental(rentalId);
    }

    @Override
    public Iterable<MovieRentalsDTO> moviesByRentNumber() throws MovieRentalsException {
        return rentalService.moviesByRentNumber();
    }

    @Override
    public Iterable<ClientRentalsDTO> clientsByRentNumber() throws MovieRentalsException {
        return rentalService.clientsByRentNumber();
    }

    @Override
    public ClientRentReportDTO generateReportByClient(Long id) throws MovieRentalsException {
        return rentalService.generateReportByClient(id);
    }

    @Override
    public MovieRentReportDTO generateReportByMovie(Long id) throws MovieRentalsException {
        return rentalService.generateReportByMovie(id);
    }
}
