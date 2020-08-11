package com.talko.ScrumBoard.service;

import com.talko.ScrumBoard.model.User;

import java.util.List;
public interface UserService {

    User register(User user);

    void deleteUser(Long id);

    void deleteAllUsers();

    User updateUser(User user);

    List<User> getAllUsers();

    User findByUsername(String username);

    User findById(Long id);
}
