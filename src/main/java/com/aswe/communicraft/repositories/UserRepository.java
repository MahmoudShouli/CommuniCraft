package com.aswe.communicraft.repositories;

import com.aswe.communicraft.models.entities.CraftEntity;
import com.aswe.communicraft.models.entities.UserEntity;
import com.aswe.communicraft.models.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUserName(String username);

    List<UserEntity> findByCraft(CraftEntity craft);

    Optional<UserEntity> findByIsLeaderAndProjectId(boolean isLeader, int projectId);
    @Modifying
    @Transactional
    @Query("SELECT u FROM UserEntity u WHERE u.role = :role")
    Optional<List<UserEntity>> findByRole(Role role);
    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.isDeleted = true WHERE u.id = :id")
    void softDeleteById(@Param("id") int userId);



    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.isLeader = true WHERE u.userName = :name")
    void makeLeader(@Param("name") String name);
}
