package com.talko.ScrumBoard.repository;

import com.talko.ScrumBoard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String name);
}
