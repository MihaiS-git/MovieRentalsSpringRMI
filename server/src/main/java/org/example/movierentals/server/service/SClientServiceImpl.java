package org.example.movierentals.server.service;

import org.example.movierentals.common.IClientService;
import org.example.movierentals.common.domain.Client;
import org.example.movierentals.common.domain.exceptions.MovieRentalsException;
import org.example.movierentals.server.repository.ClientDBRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class SClientServiceImpl implements IClientService {
    private final ClientDBRepository clientRepository;

    public SClientServiceImpl(ClientDBRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Iterable<Client> getAllClients() throws MovieRentalsException {
        return clientRepository.findAll();
    }

    @Override
    public void addClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client must not be null. ");
        }
        Optional<Client> optional = clientRepository.save(client);
        if (optional.isEmpty()) {
            throw new MovieRentalsException("Client was not saved. ");
        }
    }

    @Override
    public Client getClientById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null. ");
        }
        Optional<Client> optional = clientRepository.findOne(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new MovieRentalsException("There is no Client with Id: " + id);
        }
    }

    @Override
    public void updateClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client must not be null. ");
        }
        Optional<Client> optional = clientRepository.update(client);
        if (optional.isEmpty()) {
            throw new MovieRentalsException("Client was not updated. ");
        }
    }

    @Override
    public void deleteClientById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null. ");
        }
        Optional<Client> optional = clientRepository.delete(id);
        if (optional.isEmpty()) {
            throw new MovieRentalsException("Client with ID " + id + " not found.");
        }
    }

    @Override
    public Iterable<Client> filterClientsByKeyword(String keyword) throws MovieRentalsException {
        if (keyword == null) {
            throw new IllegalArgumentException("Keyword must not be null. ");
        }
        Iterable<Client> clients = clientRepository.findAll();
        return StreamSupport.stream(clients.spliterator(), false)
                .filter(client -> client.getFirstName().toLowerCase().contains(keyword.toLowerCase())
                        || client.getLastName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toSet());
    }
}
