package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.reponses.UserResponse;
import com.serv.oeste.application.dtos.requests.UserRegisterRequest;
import com.serv.oeste.application.dtos.requests.UserUpdateRequest;
import com.serv.oeste.domain.contracts.repositories.IUserRepository;
import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.domain.enums.Roles;
import com.serv.oeste.domain.exceptions.user.UserAlreadyInUseException;
import com.serv.oeste.domain.exceptions.user.UserNotFoundException;
import com.serv.oeste.domain.exceptions.user.UserNotValidException;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PageResponse<UserResponse> findAll(PageFilter pageFilter) {
        LOGGER.debug("user.fetch-list.started pageFilter={}", pageFilter);
        PageResponse<UserResponse> users = userRepository.findAll(pageFilter).map(UserResponse::new);
        LOGGER.debug(
                "user.fetch-list.completed totalElements={} totalPages={}",
                users.getPage().totalElements(),
                users.getPage().totalPages()
        );
        return users;
    }

    public void register(UserRegisterRequest registerRequest) {
        LOGGER.info("user.register.started username={} role={}", registerRequest.username(), registerRequest.role());

        if (registerRequest.role() == Roles.ADMIN)
            throw new UserNotValidException("Cadastro inválido, não é possível registrar um usuário ADMIN");

        if (userRepository.findByUsername(registerRequest.username()).isPresent()) {
            LOGGER.warn("user.register.username-already-in-use username={}", registerRequest.username());
            throw new UserAlreadyInUseException();
        }

        userRepository.save(new User(
                registerRequest.username(),
                passwordEncoder.encode(registerRequest.password()),
                registerRequest.role()
        ));

        LOGGER.info("user.register.completed username={} role={}", registerRequest.username(), registerRequest.role());
    }

    public void update(UserUpdateRequest updateUserRequest) {
        LOGGER.info("user.update.started id={} username={} role={}",
                updateUserRequest.id(),
                updateUserRequest.username(),
                updateUserRequest.role()
        );

        if (updateUserRequest.role() == Roles.ADMIN)
            throw new UserNotValidException("Atualização inválida, não é possível atualizar um usuário ADMIN");

        User existingUser = userRepository.findById(updateUserRequest.id())
                .orElseThrow(UserNotFoundException::new);

        if (!existingUser.getUsername().equals(updateUserRequest.username())) {
            userRepository.findByUsername(updateUserRequest.username())
                    .ifPresent(user -> {
                        LOGGER.warn("user.update.username-already-in-use currentUsername={} newUsername={}",
                                existingUser.getUsername(),
                                updateUserRequest.username()
                        );
                        throw new UserAlreadyInUseException();
                    });
        }

        existingUser.update(
                updateUserRequest.username(),
                passwordEncoder.encode(updateUserRequest.password()),
                updateUserRequest.role()
        );

        userRepository.save(existingUser);

        LOGGER.info("user.update.completed id={} username={} role={}",
                existingUser.getId(),
                existingUser.getUsername(),
                existingUser.getRole()
        );
    }

    public void delete(String username) {
        LOGGER.info("user.delete.started username={}", username);

        if (userRepository.findByUsername(username).isEmpty()) {
            LOGGER.warn("user.delete.not-found username={}", username);
            throw new UserNotFoundException();
        }

        userRepository.delete(username);
        LOGGER.info("user.delete.completed username={}", username);
    }
}
