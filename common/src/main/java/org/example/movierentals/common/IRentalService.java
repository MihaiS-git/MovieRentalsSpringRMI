package org.example.movierentals.common;

import org.example.movierentals.common.domain.*;

public interface IRentalService {
    Iterable<Rental> getAllRentals();

    Rental getRentalById(Long id);

    void rentAMovie(Rental rental);

    void updateRentalTransaction(Rental rental);

    void deleteMovieRental(Long rentalId);

    Iterable<MovieRentalsDTO> moviesByRentNumber();

    Iterable<ClientRentalsDTO> clientsByRentNumber();

    ClientRentReportDTO generateReportByClient(Long id);

    MovieRentReportDTO generateReportByMovie(Long id);
}
