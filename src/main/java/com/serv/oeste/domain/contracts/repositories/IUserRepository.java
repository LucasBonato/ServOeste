package com.serv.oeste.domain.contracts.repositories;

import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;

import java.util.Optional;

public interface IUserRepository {
    PageResponse<User> findAll(PageFilter pageFilter);
    Optional<User> findById(Integer id);
    Optional<User> findByUsername(String username);
    User save(User user);
    void delete(String username);
}
