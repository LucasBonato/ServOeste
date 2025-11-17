package com.serv.oeste.infrastructure.repositories.implementations;

import com.serv.oeste.domain.contracts.repositories.IUserRepository;
import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.domain.valueObjects.PageFilter;
import com.serv.oeste.domain.valueObjects.PageResponse;
import com.serv.oeste.infrastructure.entities.user.UserEntity;
import com.serv.oeste.infrastructure.repositories.jpa.IUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepository implements IUserRepository {
    private final IUserJpaRepository userJpaRepository;

    @Override
    public PageResponse<User> findAll(PageFilter pageFilter) {
        Sort sort = Sort.by(Sort.Direction.ASC, "username");

        Pageable pageable = PageRequest.of(pageFilter.page(), pageFilter.size(), sort);

        Page<User> usersPaged = userJpaRepository.findAll(pageable).map(UserEntity::toUser);

        return new PageResponse<>(
                usersPaged.getContent(),
                usersPaged.getTotalPages(),
                usersPaged.getNumber(),
                usersPaged.getSize()
        );
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userJpaRepository.findById(id).map(UserEntity::toUser);
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(UserEntity::toUser);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(new UserEntity(user)).toUser();
    }

    @Override
    @Transactional
    public void delete(String username) {
        userJpaRepository.deleteByUsername(username);
    }
}
