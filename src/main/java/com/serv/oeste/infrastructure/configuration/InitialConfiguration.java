package com.serv.oeste.infrastructure.configuration;

import com.serv.oeste.domain.contracts.repositories.IUserRepository;
import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.domain.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class InitialConfiguration implements CommandLineRunner {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(InitialConfiguration.class);

    @Override
    public void run(String... args) {
        String username = "admin";

        userRepository.findByUsername(username).ifPresentOrElse(
            user -> logger.info("Admin user is already created"),
            () -> userRepository.save(new User(
                    username,
                    passwordEncoder.encode("teste"),
                    Roles.ADMIN
            ))
        );
    }
}
