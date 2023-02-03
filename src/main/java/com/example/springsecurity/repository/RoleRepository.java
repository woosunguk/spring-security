package com.example.springsecurity.repository;

import com.example.springsecurity.entity.ERole;
import com.example.springsecurity.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(ERole name);
}
