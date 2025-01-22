package com.beaverbyte.financial_tracker_application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beaverbyte.financial_tracker_application.model.ERole;
import com.beaverbyte.financial_tracker_application.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}