package com.aswe.communicraft.repositories;

import com.aswe.communicraft.models.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Integer> {
    RoleEntity findByRoleName(String roleName);
}
