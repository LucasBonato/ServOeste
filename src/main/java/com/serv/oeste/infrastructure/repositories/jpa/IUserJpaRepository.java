package com.serv.oeste.infrastructure.repositories.jpa;

import com.serv.oeste.infrastructure.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserJpaRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    void deleteByUsername(String username);
}
