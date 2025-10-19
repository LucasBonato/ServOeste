package com.serv.oeste.infrastructure.repositories.jpa;

import com.serv.oeste.infrastructure.entities.user.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);

    @Modifying
    @Transactional
    @Query("""
            UPDATE RefreshTokenEntity r SET r.revokedAt = :now
            WHERE r.tokenHash = :tokenHash
            AND r.revokedAt IS NULL
            AND r.expiresAt > :now
    """)
    void revokeAllActiveForUser(@Param("tokenHash") String tokenHash, @Param("now") Instant now);
}
