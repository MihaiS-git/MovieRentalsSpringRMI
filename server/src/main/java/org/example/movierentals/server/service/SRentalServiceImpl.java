package org.example.movierentals.server.service;

import org.example.movierentals.common.IRentalService;
import org.example.movierentals.common.domain.*;
import org.example.movierentals.common.domain.exceptions.MovieRentalsException;
import org.example.movierentals.server.repository.RentalDBRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

@Service
public class SRentalServiceImpl implements IRentalService {
    private final RentalDBRepository rentalRepository;
    private final SMovieServiceImpl movieService;
    private final SClientServiceImpl clientService;

    public SRentalServiceImpl(RentalDBRepository rentalRepository,
                              SMovieServiceImpl movieService,
                              SClientServiceImpl clientService) {
        this.rentalRepository = rentalRepository;
        this.movieService = movieService;
        this.clientService = clientService;
    }


    @Override
    public Iterable<Rental> getAllRentals() throws MovieRentalsException {
        return rentalRepository.findAll();
    }

    @Override
    public Rental getRentalById(Long id) throws MovieRentalsException {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null. ");
        }
        Optional<Rental> optional = rentalRepository.findOne(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new MovieRentalsException("Rental with ID " + id + " not found.");
        }
    }

    @Override
    public void rentAMovie(Rental rental) {
        if (rental == null) {
            throw new IllegalArgumentException("Rental must not be null. ");
        }
        Optional<Rental> optional = rentalRepository.save(rental);
        if (optional.isEmpty()) {
            throw new MovieRentalsException("Rental was not saved. ");
        }
    }

    @Override
    public void updateRentalTransaction(Rental rental) {
        if (rental == null) {
            throw new IllegalArgumentException("Rental must not be null. ");
        }
        try {
            Optional<Rental> optional = rentalRepository.update(rental);
            if (optional.isEmpty()) {
                throw new MovieRentalsException("Rental was not updated. ");
            }
        } catch (DataIntegrityViolationException e) {
            throw new MovieRentalsException("Rental was not updated. Invalid ID provided. ");
        }
    }

    @Override
    public void deleteMovieRental(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null. ");
        }
        Optional<Rental> optional = rentalRepository.delete(id);
        if (optional.isEmpty()) {
            throw new MovieRentalsException("Rental with ID " + id + "not found. ");
        }
    }

    /**
     * Sort movies by the number of rents in descending order.
     *
     * @return an ordered list of movie DTO(Movie, counter).
     * @throws MovieRentalsException if there are database connection problems.
     */
    @Override
    public Iterable<MovieRentalsDTO> moviesByRentNumber() throws MovieRentalsException {
        Map<Long, Integer> mapMovieIdRentCounter = new HashMap<>();
        List<MovieRentalsDTO> moviesByRentCounterDesc = new ArrayList<>();
        try {
            Iterable<Rental> rentals = getAllRentals();
            for (Rental r : rentals) {
                Integer counter = 0;
                for (Rental rental : rentals) {
                    if (rental.getMovieId().equals(r.getMovieId())) {
                        counter++;
                    }
                }
                mapMovieIdRentCounter.put(r.getMovieId(), counter);
            }
            mapMovieIdRentCounter.forEach((k, v) -> {
                MovieRentalsDTO movieDTO = new MovieRentalsDTO(movieService.getMovieById(k), v);
                if (moviesByRentCounterDesc.isEmpty()) {
                    moviesByRentCounterDesc.add(movieDTO);
                } else {
                    boolean flag = false;
                    for (MovieRentalsDTO m : moviesByRentCounterDesc) {
                        if (m.getRentCounter() <= movieDTO.getRentCounter()) {
                            moviesByRentCounterDesc.add(moviesByRentCounterDesc.indexOf(m), movieDTO);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        moviesByRentCounterDesc.add(movieDTO);
                    }
                }
            });
            return moviesByRentCounterDesc;
        } catch (MovieRentalsException e) {
            throw new MovieRentalsException("Rental Service exception: " + e.getMessage());
        }
    }

    /**
     * Sort clients by the number of rented movies in descending order.
     *
     * @return an ordered list of client DTO(Client, counter).
     * @throws MovieRentalsException if there are database connection problems.
     */
    @Override
    public Iterable<ClientRentalsDTO> clientsByRentNumber() {
        Map<Long, Integer> mapClientIdRentedMovies = new HashMap<>();
        List<ClientRentalsDTO> orderedClientsByRentedMovies = new ArrayList<>();
        try {
            Iterable<Rental> rentals = getAllRentals();
            for (Rental r : rentals) {
                Long clientId = r.getClientId();
                Integer counter = 0;
                for (Rental rental : rentals) {
                    if (rental.getClientId().equals(clientId)) {
                        counter++;
                    }
                }
                mapClientIdRentedMovies.put(clientId, counter);
            }
            mapClientIdRentedMovies.forEach((k, v) -> {
                ClientRentalsDTO clientDTO = new ClientRentalsDTO(clientService.getClientById(k), v);
                if (orderedClientsByRentedMovies.isEmpty()) {
                    orderedClientsByRentedMovies.add(clientDTO);
                } else {
                    boolean flag = false;
                    for (ClientRentalsDTO c : orderedClientsByRentedMovies) {
                        if (c.getRentCounter() <= clientDTO.getRentCounter()) {
                            orderedClientsByRentedMovies.add(orderedClientsByRentedMovies.indexOf(c), clientDTO);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        orderedClientsByRentedMovies.add(clientDTO);
                    }
                }
            });
            return orderedClientsByRentedMovies;
        } catch (MovieRentalsException e) {
            throw new MovieRentalsException("Rental Service Exception " + e.getMessage());
        }
    }

    /**
     * Generate report of the rented movies, total charges and rent dates for a given Client.
     *
     * @param id must not be null.
     * @return the result DTO.
     */
    @Override
    public ClientRentReportDTO generateReportByClient(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null. ");
        }
        List<Movie> moviesList = new ArrayList<>();
        List<LocalDateTime> rentDates = new ArrayList<>();
        float totalCharges = 0.00f;
        int counter = 0;
        Client client = clientService.getClientById(id);
        try {
            Predicate<Rental> clientIdFilter = rental -> rental.getClientId().equals(id);
            rentalRepository.findAll().forEach(rental -> {
                if (clientIdFilter.test(rental)) {
                    moviesList.add(movieService.getMovieById(rental.getMovieId()));
                    rentDates.add(rental.getRentalDate());
                }
            });
            for (Rental rental :
                    rentalRepository.findAll()) {
                if (clientIdFilter.test(rental)) {
                    totalCharges += rental.getRentalCharge();
                    counter++;
                }
            }
        } catch (MovieRentalsException e) {
            throw new MovieRentalsException("Rental Service exception: " + e.getMessage());
        }
        return new ClientRentReportDTO(client, moviesList, totalCharges, rentDates, counter);
    }

    /**
     * Generate report of the rented movies, total charges and rent dates for a given Movie.
     *
     * @param id must not be null
     * @return the result DTO.
     */
    @Override
    public MovieRentReportDTO generateReportByMovie(Long id) {
        if (id == null) {
            throw new MovieRentalsException("Id must not be null. ");
        }
        List<Client> clientList = new ArrayList<>();
        List<LocalDateTime> rentDates = new ArrayList<>();
        float totalCharges = 0.00f;
        int counter = 0;
        Movie movie = movieService.getMovieById(id);
        try {
            Predicate<Rental> movieIdFilter = rental -> rental.getMovieId().equals(id);
            rentalRepository.findAll().forEach(rental -> {
                if (movieIdFilter.test(rental)) {
                    clientList.add(clientService.getClientById(rental.getClientId()));
                    rentDates.add(rental.getRentalDate());
                }
            });
            for (Rental rental :
                    rentalRepository.findAll()) {
                if (movieIdFilter.test(rental)) {
                    totalCharges += rental.getRentalCharge();
                    counter++;
                }
            }
        } catch (MovieRentalsException e) {
            throw new MovieRentalsException("Rental Service Exception: " + e.getMessage());
        }
        return new MovieRentReportDTO(movie, clientList, totalCharges, rentDates, counter);
    }
}

