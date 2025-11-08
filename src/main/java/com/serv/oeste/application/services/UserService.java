package com.serv.oeste.application.services;

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

    public PageResponse<User> findAll(PageFilter pageFilter) {
        return userRepository.findAll(pageFilter);
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
            throw new UserNotValidException("Cadastro inválido, não é possível registrar um usuário ADMIN");

        if (userRepository.findByUsername(updateUserRequest.username()).isPresent())
            throw new UserAlreadyInUseException();

        userRepository.save(updateUserRequest.toDomain());
    }

    public void delete(String username) {
        if (userRepository.findByUsername(username).isEmpty())
            throw new UserNotFoundException();

        userRepository.delete(username);
    }
}
