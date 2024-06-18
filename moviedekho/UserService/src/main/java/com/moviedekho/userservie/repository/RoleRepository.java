package com.moviedekho.userservie.repository;

import com.moviedekho.userservie.entity.RoleEntity;
import com.moviedekho.userservie.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(RoleName roleName);
}
