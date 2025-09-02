package com.serv.oeste.domain.contracts.repositories;

import com.serv.oeste.domain.entities.user.User;

import java.util.Optional;

public interface IUserRepository {
    Optional<User> findByUsername(String username);
    User save(User user);
}
