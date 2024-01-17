package org.example.movierentals.server.config;

import org.example.movierentals.common.IClientService;
import org.example.movierentals.common.IMovieService;
import org.example.movierentals.common.IRentalService;
import org.example.movierentals.server.repository.ClientDBRepository;
import org.example.movierentals.server.repository.MovieDBRepository;
import org.example.movierentals.server.repository.RentalDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

@Configuration
@ComponentScan(basePackages = {"org.example.movierentals.server.service", "org.example.movierentals.server.config"})
public class ServerConfig {

//    @Autowired
//    @Qualifier("SMovieServiceImpl")
//    private IMovieService movieService;
//
//    @Autowired
//    @Qualifier("SClientServiceImpl")
//    private IClientService clientService;
//
//    @Autowired
//    @Qualifier("SRentalServiceImpl")
//    private IRentalService rentalService;

    @Bean
    public MovieDBRepository movieDBRepository() {
        return new MovieDBRepository();
    }

    @Bean
    public ClientDBRepository clientDBRepository() {
        return new ClientDBRepository();
    }

    @Bean
    public RentalDBRepository rentalDBRepository() {
        return new RentalDBRepository();
    }

    @Bean
    public RmiServiceExporter movieRmiServiceExporter(@Qualifier("SMovieServiceImpl") IMovieService movieService) {
        System.out.println("Exporting movieService to RMI registry");
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("movieService");
        exporter.setServiceInterface(IMovieService.class);
        exporter.setService(movieService);
        return exporter;
    }

    @Bean
    public RmiServiceExporter clientRmiServiceExporter(@Qualifier("SClientServiceImpl") IClientService clientService) {
        System.out.println("Exporting clientService to RMI registry");
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("clientService");
        exporter.setServiceInterface(IClientService.class);
        exporter.setService(clientService);
        return exporter;
    }

    @Bean
    public RmiServiceExporter rentalRmiServiceExporter(@Qualifier("SRentalServiceImpl") IRentalService rentalService) {
        System.out.println("Exporting rentalService to RMI registry");
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("rentalService");
        exporter.setServiceInterface(IRentalService.class);
        exporter.setService(rentalService);
        return exporter;
    }
}
