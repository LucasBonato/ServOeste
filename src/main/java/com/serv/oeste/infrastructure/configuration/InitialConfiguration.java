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
    private static final Logger LOGGER = LoggerFactory.getLogger(InitialConfiguration.class);

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminUserProperties adminProperties;

    @Override
    public void run(String... args) {
        userRepository.findByUsername(adminProperties.username()).ifPresentOrElse(
                user -> LOGGER.info("admin-user.check.exists username={}", adminProperties.username()),
                () -> {
                    userRepository.save(new User(
                            adminProperties.username(),
                            passwordEncoder.encode(adminProperties.password()),
                            Roles.ADMIN
                    ));
                    LOGGER.info("admin-user.created username={}", adminProperties.username());
                }
        );
    }
}
