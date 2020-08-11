package com.talko.ScrumBoard.security;

import com.talko.ScrumBoard.model.User;
import com.talko.ScrumBoard.security.jwt.JwtUser;
import com.talko.ScrumBoard.security.jwt.JwtUserFactory;
import com.talko.ScrumBoard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with username - " + username + " not found");
        }

        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("IN loadUserByUsername user with username {} successfully loaded", username);
        return jwtUser;
    }
}
