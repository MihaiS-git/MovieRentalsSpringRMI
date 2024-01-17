package org.example.movierentals.common.domain;

import java.io.Serializable;

public class ClientRentalsDTO implements Serializable {
    Client client;
    int rentCounter;

    public ClientRentalsDTO() {
    }

    public ClientRentalsDTO(Client client, int rentCounter) {
        this.client = client;
        this.rentCounter = rentCounter;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getRentCounter() {
        return rentCounter;
    }

    public void setRentCounter(int rentCounter) {
        this.rentCounter = rentCounter;
    }

    @Override
    public String toString() {
        return "ClientRentalsDTO{" +
                "client=" + client +
                ", rentCounter=" + rentCounter +
                '}';
    }
}
