package com.talko.ScrumBoard.repository;

import com.talko.ScrumBoard.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
