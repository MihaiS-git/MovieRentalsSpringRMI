package org.example.movierentals.ui;

import org.example.movierentals.common.IClientService;
import org.example.movierentals.common.IMovieService;
import org.example.movierentals.common.IRentalService;
import org.example.movierentals.common.domain.*;
import org.example.movierentals.common.domain.exceptions.MovieRentalsException;
import org.example.movierentals.service.CClientServiceImpl;
import org.example.movierentals.service.CMovieServiceImpl;
import org.example.movierentals.service.CRentalServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;


public class Console {
    Scanner scanner = new Scanner(System.in);
    private final IMovieService movieService;
    private final IClientService clientService;
    private final IRentalService rentalService;

    public Console(IMovieService movieService, IClientService clientService, IRentalService rentalService) {
        this.movieService = movieService;
        this.clientService = clientService;
        this.rentalService = rentalService;
    }

    /**
     * Run Console
     */
    public void runConsole() {
        while (true) {
            this.showMenu();
            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        this.runSubMenuMovies();
                        break;
                    case 2:
                        this.runSubMenuClients();
                        break;
                    case 3:
                        this.runSubMenuRentals();
                        break;
                    case 0:
                        return;
                    default:
                        System.err.println("Unsupported command.");
                }
            } else {
                String invalidInput = scanner.next();
                System.err.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Print the General Menu
     */
    public void showMenu() {
        System.out.println();
        System.out.println("MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Movies Menu");
        System.out.println("2. Clients Menu");
        System.out.println("3. Rent Movie & Reports Menu");
        System.out.println("0. Exit");
        System.out.print("\nEnter your option: ");
    }

    /**
     * Run the Movies submenu.
     */
    private void runSubMenuMovies() {
        while (true) {
            System.out.println();
            System.out.println("MOVIES MENU");
            System.out.println("=".repeat(50));
            System.out.println("1. Add Movie");
            System.out.println("2. Print Movie");
            System.out.println("3. Print All Movies");
            System.out.println("4. Update Movie");
            System.out.println("5. Delete Movie");
            System.out.println("6. Filter Movies by Keyword");
            System.out.println("0. Back");
            System.out.print("\nEnter your option: ");

            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        this.handleAddMovie();
                        break;
                    case 2:
                        this.handlePrintMovie();
                        break;
                    case 3:
                        this.handleGetAllMovies();
                        break;
                    case 4:
                        this.handleUpdateMovie();
                        break;
                    case 5:
                        this.handleDeleteMovieById();
                        break;
                    case 6:
                        this.handleFilterMoviesByKeyword();
                        break;
                    case 0:
                        return;
                    default:
                        System.err.println("Unsupported command.");
                }
            } else {
                String invalidInput = scanner.next();
                System.err.println("Invalid input. Please enter a valid number");
            }
        }
    }

    /**
     * Handle filter Movies by Keyword.
     */
    private void handleFilterMoviesByKeyword() {
        System.out.println("Enter the filter Keyword: ");
        String keyword = scanner.next();

        try {
            Set<Movie> filteredMovies = (Set<Movie>) movieService.filterMoviesByKeyword(keyword);
            if (!filteredMovies.isEmpty()) {
                System.out.println("Filtered movies by keyword: " + keyword);
                System.out.println("=".repeat(50));
                filteredMovies.forEach(System.out::println);
            } else {
                System.err.println("No movies found for the provided keyword. ");
            }
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Handle update Movie feature
     */
    private void handleUpdateMovie() {
        System.out.print("Enter the ID of the Movie: ");
        Long id = null;
        while (id == null) {
            if (scanner.hasNextLong()) {
                id = scanner.nextLong();
            }
        }
        try {
            while (movieService.getMovieById(id) != null) {
                try {
                    Movie movie = readMovie();
                    movie.setId(id);
                    movieService.updateMovie(movie);
                    System.out.println("Successfully updated. ");
                    break;
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Handle delete Movie feature.
     */
    private void handleDeleteMovieById() {
        System.out.print("Enter the id of the Movie: ");
        Long id = null;
        while (id == null) {
            if (scanner.hasNextLong()) {
                id = scanner.nextLong();
            }
        }
        try {
            movieService.deleteMovieById(id);
            System.out.println("Successfully deleted. ");
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Handle print Movie by ID feature.
     */
    private void handlePrintMovie() {
        System.out.print("Enter the id of the Movie: ");
        Long id = null;
        while (id == null) {
            if (scanner.hasNextLong()) {
                id = scanner.nextLong();
            }
        }
        try {
            Movie movie = movieService.getMovieById(id);
            System.out.println(movie);
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Handle save new Movie.
     */
    private void handleAddMovie() {
        try {
            Movie movie = readMovie();
            try {
                movieService.addMovie(movie);
                System.out.println("Successfully saved. ");
            } catch (MovieRentalsException e) {
                System.err.println(e.getMessage());
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    /**
     * Read user entered info for a Movie.
     *
     * @return a Movie entity.
     */
    private Movie readMovie() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Read Movie Title
        String title = "";
        while (title.isEmpty()) {
            System.out.print("Enter the movie title: ");
            title = reader.readLine().trim();
        }

        // Read Movie Year
        int year = 0;
        boolean validYear = false;
        while (!validYear) {
            System.out.print("Enter the year of the movie: ");
            try {
                year = Integer.parseInt(reader.readLine().trim());
                validYear = true;
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a valid (int) year. ");
            }
        }

        // Read Movie Genre
        MovieGenres genre = null;
        while (genre == null) {
            System.out.print("Enter the genre of the movie" +
                    "(Action/Comedy/Drama/Fantasy/Horror/Mystery/Romance/Thriller/Western): ");
            String genreInput = reader.readLine().trim();
            try {
                genre = MovieGenres.valueOf(genreInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid input. Please enter a valid Movie genre. ");
            }
        }

        // Read Movie AgeRestriction
        AgeRestrictions ageRestriction = null;
        while (ageRestriction == null) {
            System.out.print("Enter the age restrictions of the movie(GA/PG/PG13/R/NC17): ");
            String ageRestrictionInput = reader.readLine().trim();
            try {
                ageRestriction = AgeRestrictions.valueOf(ageRestrictionInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid input. Please enter a valid Movie age restriction.");
            }
        }

        // Read Movie price for rent.
        float rentalPrice = 0.0f;
        boolean validRentalPrice = false;
        while (!validRentalPrice) {
            System.out.print("Enter the price for rent of the Movie: ");
            try {
                rentalPrice = Float.parseFloat(reader.readLine().trim());
                validRentalPrice = true;
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a valid (Float) price.");
            }
        }

        // Read Movie availability
        boolean available;
        while (true) {
            System.out.print("Is it available for rent?(true/false): ");
            String availableInput = reader.readLine();
            if (availableInput.trim().equalsIgnoreCase("false") || availableInput.trim().equalsIgnoreCase("true")) {
                available = Boolean.parseBoolean(availableInput);
                break;
            } else {
                System.err.println("Invalid input. Please enter 'true' or 'false'.");
            }
        }

        return new Movie(title, year, genre, ageRestriction, rentalPrice, available);
    }

    /**
     * Handle Get All Movies feature.
     */
    private void handleGetAllMovies() {
        try {
            movieService.getAllMovies().forEach(System.out::println);
        } catch (MovieRentalsException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Run the Clients submenu.
     */
    private void runSubMenuClients() {
        while (true) {
            System.out.println();
            System.out.println("CLIENTS MENU");
            System.out.println("=".repeat(50));
            System.out.println("1. Add Client");
            System.out.println("2. Print Client");
            System.out.println("3. Print All Clients");
            System.out.println("4. Update Client");
            System.out.println("5. Delete Client");
            System.out.println("6. Filter Client");
            System.out.println("0. Back");
            System.out.print("\nEnter your option: ");

            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        this.handleAddClient();
                        break;
                    case 2:
                        this.handlePrintClient();
                        break;
                    case 3:
                        this.handleGetAllClients();
                        break;
                    case 4:
                        this.handleUpdateClient();
                        break;
                    case 5:
                        this.handleDeleteClientByID();
                        break;
                    case 6:
                        this.handleFilterClientsByKeyword();
                        break;
                    case 0:
                        return;
                    default:
                        System.err.println("Unsupported command.");
                }
            } else {
                String invalidInput = scanner.next();
                System.err.println("Invalid input. Please enter a valid number!");
            }


        }
    }

    private void handleGetAllClients() {
        try {
            clientService.getAllClients().forEach(System.out::println);
        } catch (MovieRentalsException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handle Add Client feature.
     */
    private void handleAddClient() {

        try {
            Client client = readClient();
            try {
                clientService.addClient(client);
                System.out.println("Successfully added client.");
            } catch (MovieRentalsException e) {
                System.err.println("An error occurred: " + e.getMessage() + "\n");
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());

        }
    }

    /**
     * Read Client from System.in
     *
     * @return Client
     */
    private Client readClient() {

        scanner.nextLine();
        //Read First Name of the Client
        System.out.print("Enter firstName: ");
        String firstName = scanner.nextLine();

        //Read Last Name of the Client
        System.out.print("Enter lastName: ");
        String lastName = scanner.nextLine();

        //Read Date Of Birth of the Client by format yyy-MM-dd
        System.out.print("Enter dateOfBirth:yyyy-MM-dd: ");
        String dateOfBirth = scanner.nextLine();

        //Read the email of the Client
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        //Read Boolean for subscribe of the Client
        System.out.print("Do you want to subscribe?:true/false ");
        Boolean subscribe = scanner.nextBoolean();

        return new Client(firstName, lastName, dateOfBirth, email, subscribe);
    }

    /**
     * Handle Update Client by ID feature
     */
    private void handleUpdateClient() {
        System.out.println("Enter the id of the client: ");
        Long id = null;
        while (id == null) {
            if (scanner.hasNextLong()) {
                id = scanner.nextLong();
            }
        }
        try {
            while (clientService.getClientById(id) != null) {
                try {
                    Client client = readClient();
                    client.setIdClient(id);
                    clientService.updateClient(client);
                    System.out.println("Successfully updated client.");
                    break;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (MovieRentalsException e) {
            System.err.println("An error occurred: " + e.getMessage() + "\n");
        }
    }

    /**
     * Handle Print Client by ID feature.
     */
    private void handlePrintClient() {
        System.out.print("Enter the id of the Client: ");
        Long id = null;
        while (id == null) {
            if (scanner.hasNextLong()) {
                id = scanner.nextLong();
            }
        }

        try {
            System.out.println(clientService.getClientById(id));

        } catch (MovieRentalsException e) {
            System.err.println("An error occurred: " + e.getMessage() + "\n");
        }
    }


    /**
     * Handle Delete Client by ID feature.
     */
    private void handleDeleteClientByID() {
        System.out.println("Enter the id of the Client: ");
        Long id = null;
        while (id == null) {
            if (scanner.hasNextLong()) {
                id = scanner.nextLong();
            }
        }
        try {
            clientService.deleteClientById(id);
            System.out.println("Successfully deleted client. ");
        } catch (MovieRentalsException e) {
            System.err.println("An error occurred: " + e.getMessage() + "\n");
        }
    }

    /**
     * Handle filter Clients by Keyword.
     */
    private void handleFilterClientsByKeyword() {
        System.out.println("Enter the filter Keyword: ");
        String keyword = scanner.next();

        try {
            Set<Client> filteredClients = (Set<Client>) clientService.filterClientsByKeyword(keyword);
            if (!filteredClients.isEmpty()) {
                System.out.println("Filtered clients by keyword: " + keyword);
                System.out.println("=".repeat(50));
                filteredClients.forEach(System.out::println);
            } else {
                System.err.println("No clients found for the provided keyword. ");
            }
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }


    /**
     * Run Rentals submenu.
     */
    private void runSubMenuRentals() {
        while (true) {
            System.out.println();
            System.out.println("RENT & REPORTS MENU");
            System.out.println("=".repeat(50));
            System.out.println("1. Print a rent transaction by ID");
            System.out.println("2. Print all rent transactions");
            System.out.println("3. Rent a Movie");
            System.out.println("4. Update a Rent Transaction");
            System.out.println("5. Delete a Rent Transaction");
            System.out.println("6. Print Movies by Rent Counter");
            System.out.println("7. Print Clients by Rent Counter");
            System.out.println("8. Print Client Rent Report by ID");
            System.out.println("9. Print Movie Rent Report by ID");
            System.out.println("0. Back");
            System.out.print("\nEnter your option: ");

            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        this.handlePrintRental();
                        break;
                    case 2:
                        this.handlePrintAllRentals();
                        break;
                    case 3:
                        this.handleRentAMovie();
                        break;
                    case 4:
                        this.handleUpdateRentTransaction();
                        break;
                    case 5:
                        this.handleDeleteRentTransaction();
                        break;
                    case 6:
                        this.handleMoviesByRentals();
                        break;
                    case 7:
                        this.handleClientsByRentedMovies();
                        break;
                    case 8:
                        this.handleClientRentReport();
                        break;
                    case 9:
                        this.handleMovieRentReport();
                        break;
                    case 0:
                        return;
                    default:
                        System.err.println("Unsupported command.");
                }
            } else {
                String invalidInput = scanner.next();
                System.err.println("Invalid input. Please enter a valid number!");
            }
        }
    }

    /**
     * Handle update Rental transaction
     */
    private void handleUpdateRentTransaction() {
        Long id = null;
        while (id == null) {
            System.out.print("Enter the ID of the Rent Transaction: ");
            if (scanner.hasNextLong()) {
                id = scanner.nextLong();
            }
        }
        try {
            while (rentalService.getRentalById(id) != null) {
                Rental rental = readRentTransaction();
                rental.setId(id);
                rentalService.updateRentalTransaction(rental);
                System.out.println("Successfully updated. ");
                break;
            }
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Handle delete Rental by ID.
     */
    private void handleDeleteRentTransaction() {
        Long rentalId = 0L;
        boolean validRentalId = false;
        while (!validRentalId) {
            System.out.print("Enter the rental transaction ID: ");
            try {
                rentalId = scanner.nextLong();
                validRentalId = true;
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a valid (Long) rental transaction ID. ");
            }
        }
        try {
            rentalService.deleteMovieRental(rentalId);
            System.out.println("Successfully deleted.");
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Handle print all Rentals from Repository
     */
    private void handlePrintAllRentals() {
        try {
            rentalService.getAllRentals().forEach(System.out::println);
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Handle print a Rental by ID
     */
    private void handlePrintRental() {
        System.out.print("Enter the id of the Rental transaction: ");
        Long id = null;
        while (id == null) {
            if (scanner.hasNextLong()) {
                id = scanner.nextLong();
            }
        }

        try {
            System.out.println(rentalService.getRentalById(id));
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Handle save a new Rent transaction
     */
    private void handleRentAMovie() {
        Rental rental = readRentTransaction();
        try {
            rentalService.rentAMovie(rental);
            System.out.println("Successfully saved.");
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Handle the read of a Transaction from Keyboard.
     *
     * @return a new Rental.
     */
    private Rental readRentTransaction() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Read Movie ID
        Long movieId = 0L;
        boolean validMovieId = false;
        while (!validMovieId) {
            System.out.print("Enter the movie ID: ");
            try {
                movieId = Long.parseLong(reader.readLine().trim());
                validMovieId = true;
            } catch (IOException e) {
                System.err.println("Read exception: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a valid (Long) movie ID. ");
            }
        }

        // Read Client ID
        Long clientId = 0L;
        boolean validClientId = false;
        while (!validClientId) {
            System.out.print("Enter the client ID: ");
            try {
                clientId = Long.parseLong(reader.readLine().trim());
                validClientId = true;
            } catch (IOException e) {
                System.err.println("Read exception: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a valid (Long) client ID. ");
            }
        }

        // Rental Charge
        float rentalCharge = movieService.getMovieById(movieId).getRentalPrice();

        // RentalDate
        LocalDateTime rentalDate = LocalDateTime.now();

        // DueDate
        LocalDateTime dueDate = rentalDate.plusDays(1);

        return new Rental(movieId, clientId, rentalCharge, rentalDate, dueDate);
    }

    private void handleClientsByRentedMovies() {
        try {
            rentalService.clientsByRentNumber().forEach(System.out::println);
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleMoviesByRentals() {
        try {
            rentalService.moviesByRentNumber().forEach(System.out::println);
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleMovieRentReport() {
        Long id = null;
        while (id == null) {
            System.out.print("Enter the ID of the Movie: ");
            if (scanner.hasNextInt()) {
                id = scanner.nextLong();
            }
        }

        try {
            MovieRentReportDTO movieDTO = rentalService.generateReportByMovie(id);
            System.out.println("\nMOVIE #" + id + " RENT REPORT");
            System.out.println("*".repeat(50));
            System.out.println("Movie information: " + movieDTO.getMovie());
            System.out.println("List of Clients: " + movieDTO.getClientsList());
            System.out.println("Total Charges: $" + movieDTO.getTotalCharges());
            System.out.println("Rent Dates List: " + movieDTO.getRentDates());
            System.out.println("Total number of rents: " + movieDTO.getCounter());
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleClientRentReport() {
        Long id = null;
        while (id == null) {
            System.out.print("Enter the ID of the Client: ");
            if (scanner.hasNextLong()) {
                id = scanner.nextLong();
            }
        }
        try {
            ClientRentReportDTO clientReport = rentalService.generateReportByClient(id);
            System.out.println("\nCLIENT #" + id + " RENT REPORT");
            System.out.println("*".repeat(50));
            System.out.println("Client information: " + clientReport.getClient());
            System.out.println("List of rented Movies: " + clientReport.getMoviesList());
            System.out.println("Total Charges: $" + clientReport.getTotalCharges());
            System.out.println("Rent Dates List: " + clientReport.getRentDates());
            System.out.println("Total Number of Rents: " + clientReport.getCounter());
        } catch (MovieRentalsException e) {
            System.err.println(e.getMessage());
        }
    }
}
