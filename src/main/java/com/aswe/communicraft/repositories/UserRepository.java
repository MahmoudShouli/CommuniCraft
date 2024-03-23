package com.aswe.communicraft.repositories;

import com.aswe.communicraft.models.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUserName(String username);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.isDeleted = true WHERE u.id = :id")
    void softDeleteById(@Param("id") int userId);
}
