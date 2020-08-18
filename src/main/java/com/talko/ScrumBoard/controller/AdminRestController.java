package com.talko.ScrumBoard.rest;

import com.talko.ScrumBoard.dto.AdminUserDto;
import com.talko.ScrumBoard.model.User;
import com.talko.ScrumBoard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/admin/")
@RequiredArgsConstructor
public class AdminRestController {

    private final UserService userService;

    @GetMapping(value = "/users/{id}")
    private ResponseEntity<AdminUserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AdminUserDto adminUserDto = AdminUserDto.fromUser(user);

        return new ResponseEntity<>(adminUserDto, HttpStatus.OK);
    }
}
