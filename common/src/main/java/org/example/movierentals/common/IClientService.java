package org.example.movierentals.common;


import org.example.movierentals.common.domain.Client;

public interface IClientService {

    Iterable<Client> getAllClients();

    void addClient(Client client);

    Client getClientById(Long id);

    void updateClient(Client client);

    void deleteClientById(Long id);

    Iterable<Client> filterClientsByKeyword(String keyword);
}

