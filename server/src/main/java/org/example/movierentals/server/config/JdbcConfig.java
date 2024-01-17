package org.example.movierentals.server.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {
    private final String HOSTNAME = "cornelius.db.elephantsql.com";
    private final String PORT = "5432";
    private final String DATABASENAME = "vqiikuda";
    private final String USERNAME = System.getenv("username");
    private final String PASSWORD = System.getenv("password");

    @Bean
    JdbcOperations jdbcOperations() {
        System.out.println("Creating JdbcOperations bean...");
        JdbcTemplate jdbcOperations = new JdbcTemplate();
        jdbcOperations.setDataSource(dataSource());
        return jdbcOperations;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://" + HOSTNAME + ":" + PORT + "/" + DATABASENAME);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setInitialSize(2);
        return dataSource;
    }
}
