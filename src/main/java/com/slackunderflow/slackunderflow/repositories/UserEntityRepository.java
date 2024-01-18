package com.slackunderflow.slackunderflow.repositories;

import com.slackunderflow.slackunderflow.enums.BadgeEnum;
import com.slackunderflow.slackunderflow.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String email);

    long deleteByUsername(String username);

    /*@Modifying
    @Query(value = "UPDATE UserEntity u SET u.badge = :badge WHERE u.id = :userId")
    void setBadge(@Param("badge") BadgeEnum badge, @Param("userId") Long userId);*/


}
