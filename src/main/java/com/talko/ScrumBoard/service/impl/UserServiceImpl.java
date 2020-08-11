package com.talko.ScrumBoard.service.impl;

import com.talko.ScrumBoard.model.Role;
import com.talko.ScrumBoard.model.Status;
import com.talko.ScrumBoard.model.User;
import com.talko.ScrumBoard.repository.RoleRepository;
import com.talko.ScrumBoard.service.UserService;
import com.talko.ScrumBoard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);

        User registeredUser = userRepository.save(user);
        log.info("IN register - user: {} successfully registered" , registeredUser);

        return registeredUser;
    }

    @Override
    public void deleteUser(Long id) { userRepository.deleteById(id); }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.size() == 0) {
            log.warn("IN getAllUsers - no users found");
            return null;
        }

        log.info("IN getAllUsers - {} users found", users.size());

        return users;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            log.warn("IN findByUsername - no user found by username - {}", user);
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            log.warn("IN findById - no user found by id - {}", user);
            return null;
        }

        log.info("IN findById - user found by id - {}", user);
        return user;
    }
}
