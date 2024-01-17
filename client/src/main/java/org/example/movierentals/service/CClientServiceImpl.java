package org.example.movierentals.service;

import org.example.movierentals.common.IClientService;
import org.example.movierentals.common.domain.Client;
import org.example.movierentals.common.domain.exceptions.MovieRentalsException;
import org.springframework.beans.factory.annotation.Autowired;


public class CClientServiceImpl implements IClientService {

    @Autowired
    private IClientService clientService;

    @Override
    public Iterable<Client> getAllClients() throws MovieRentalsException {
        return clientService.getAllClients();
    }

    @Override
    public void addClient(Client client) throws MovieRentalsException  {

        clientService.addClient(client);
    }

    @Override
    public Client getClientById(Long id) throws MovieRentalsException {
        return clientService.getClientById(id);
    }


    @Override
    public void updateClient(Client client) throws MovieRentalsException {
        clientService.updateClient(client);

    }

    @Override
    public void deleteClientById(Long id) throws MovieRentalsException  {
        clientService.deleteClientById(id);

    }

    @Override
    public Iterable<Client> filterClientsByKeyword(String keyword) throws MovieRentalsException {
        return clientService.filterClientsByKeyword(keyword);
    }
}
