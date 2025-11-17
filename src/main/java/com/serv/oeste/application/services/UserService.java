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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PageResponse<UserResponse> findAll(PageFilter pageFilter) {
        return userRepository.findAll(pageFilter).map(UserResponse::new);
    }

    public void register(UserRegisterRequest registerRequest) {
        if (registerRequest.role() == Roles.ADMIN)
            throw new UserNotValidException("Cadastro inválido, não é possível registrar um usuário ADMIN");

        if (userRepository.findByUsername(registerRequest.username()).isPresent())
            throw new UserAlreadyInUseException();

        userRepository.save(new User(
                registerRequest.username(),
                passwordEncoder.encode(registerRequest.password()),
                registerRequest.role()
        ));
    }

    public void update(UserUpdateRequest updateUserRequest) {
        if (updateUserRequest.role() == Roles.ADMIN)
            throw new UserNotValidException("Atualização inválida, não é possível atualizar um usuário ADMIN");

        User existingUser = userRepository.findById(updateUserRequest.id())
                .orElseThrow(UserNotFoundException::new);

        if (!existingUser.getUsername().equals(updateUserRequest.username())) {
            userRepository.findByUsername(updateUserRequest.username())
                    .ifPresent(user -> {
                        throw new UserAlreadyInUseException();
                    });
        }

        existingUser.update(
                updateUserRequest.username(),
                passwordEncoder.encode(updateUserRequest.password()),
                updateUserRequest.role()
        );

        userRepository.save(existingUser);
    }

    public void delete(String username) {
        if (userRepository.findByUsername(username).isEmpty())
            throw new UserNotFoundException();

        userRepository.delete(username);
    }
}
