package org.example.movierentals.config;

import org.example.movierentals.common.IClientService;
import org.example.movierentals.common.IMovieService;
import org.example.movierentals.common.IRentalService;
import org.example.movierentals.service.CClientServiceImpl;
import org.example.movierentals.service.CMovieServiceImpl;
import org.example.movierentals.service.CRentalServiceImpl;
import org.example.movierentals.ui.Console;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Configuration
public class ClientAppConfig {
    @Bean
    RmiProxyFactoryBean movieServiceProxy() {
        RmiProxyFactoryBean movieRmiProxyFactoryBean = new RmiProxyFactoryBean();
        movieRmiProxyFactoryBean.setServiceInterface(IMovieService.class);
        movieRmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/movieService");
        return movieRmiProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean clientServiceProxy() {
        RmiProxyFactoryBean clientRmiProxyFactoryBean = new RmiProxyFactoryBean();
        clientRmiProxyFactoryBean.setServiceInterface(IClientService.class);
        clientRmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/clientService");
        return clientRmiProxyFactoryBean;
    }

    @Bean
    RmiProxyFactoryBean rentalServiceProxy() {
        RmiProxyFactoryBean rentalRmiProxyFactoryBean = new RmiProxyFactoryBean();
        rentalRmiProxyFactoryBean.setServiceInterface(IRentalService.class);
        rentalRmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/rentalService");
        return rentalRmiProxyFactoryBean;
    }

    @Bean
    Console console() {
        return new Console(movieService(), clientService(), rentalService());
    }

    @Bean
    CMovieServiceImpl movieService() {
        return new CMovieServiceImpl();
    }

    @Bean
    CClientServiceImpl clientService() {
        return new CClientServiceImpl();
    }

    @Bean
    CRentalServiceImpl rentalService() {
        return new CRentalServiceImpl();
    }
}
