package org.example.movierentals.common.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Rental implements Serializable {
    private Long idRental;
    private Long movieId;
    private Long clientId;
    private float rentalCharge;
    private LocalDateTime rentalDate;
    private LocalDateTime dueDate;

    public Rental() {
    }

    public Rental(Long movieId, Long clientId, float rentalCharge, LocalDateTime rentalDate, LocalDateTime dueDate) {
        this.movieId = movieId;
        this.clientId = clientId;
        this.rentalCharge = rentalCharge;
        this.rentalDate = rentalDate;
        this.dueDate = dueDate;
    }

    public Long getId() {
        return idRental;
    }

    public void setId(Long idRental) {
        this.idRental = idRental;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public float getRentalCharge() {
        return rentalCharge;
    }

    public void setRentalCharge(float rentalCharge) {
        this.rentalCharge = rentalCharge;
    }

    public LocalDateTime getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDateTime rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "idRental=" + idRental +
                ", movieId=" + movieId +
                ", clientId=" + clientId +
                ", rentalCharge=" + rentalCharge +
                ", rentalDate=" + rentalDate +
                ", dueDate=" + dueDate +
                '}';
    }
}
