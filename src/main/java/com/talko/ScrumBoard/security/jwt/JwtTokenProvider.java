package com.talko.ScrumBoard.security.jwt;

import com.talko.ScrumBoard.model.Role;
import com.talko.ScrumBoard.security.jwt.Exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;

    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public  String createToken(String username, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getRoleName(roles));

        Date currentDate = new Date();
        Date validity = new Date(currentDate.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.ES256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            return !claimsJws.getBody().getExpiration().before(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token expired or invalid");
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    private List<String> getRoleName(List<Role> userRoles) {
        List<String> result = new ArrayList<>();

        userRoles.forEach(role -> result.add(role.getName()));

        return result;
    }
}
