package com.impulsofit.repository;

import com.impulsofit.model.Role;
import com.impulsofit.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNombre(RoleType nomnbre);
}
