package com.serv.oeste.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class FlywayConfiguration {
    @Bean
    @DependsOn("entityManagerFactory")
    public Flyway flyway() {
        return Flyway.configure()
                .callbacks(new HibernateReadyCallback())
                .load();
    }
}
