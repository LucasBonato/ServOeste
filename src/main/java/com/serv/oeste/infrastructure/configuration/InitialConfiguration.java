package com.serv.oeste.infrastructure.configuration;

import com.serv.oeste.domain.contracts.repositories.IUserRepository;
import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.domain.enums.Roles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitialConfiguration implements CommandLineRunner {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminUserProperties adminProperties;

    @Override
    public void run(String @NonNull ... args) {
        userRepository.findByUsername(adminProperties.username()).ifPresentOrElse(
                user -> log.info("admin-user.check.exists username={}", adminProperties.username()),
                () -> {
                    userRepository.save(new User(
                            adminProperties.username(),
                            passwordEncoder.encode(adminProperties.password()),
                            Roles.ADMIN
                    ));
                    log.info("admin-user.created username={}", adminProperties.username());
                }
        );
    }
}
