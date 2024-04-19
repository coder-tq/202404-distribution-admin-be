package com.codertq.selleradmin.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * author: coder_tq
 * date: 2024/4/15
 */
public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    Date extractExpirationDate(String token);
}
