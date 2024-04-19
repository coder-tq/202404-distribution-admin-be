package com.codertq.selleradmin.service.impl;

import com.codertq.selleradmin.domain.pojo.User;
import com.codertq.selleradmin.domain.vo.request.SignInRequest;
import com.codertq.selleradmin.domain.vo.request.SignUpRequest;
import com.codertq.selleradmin.domain.vo.response.SignInResponse;
import com.codertq.selleradmin.service.AuthenticationService;
import com.codertq.selleradmin.service.JwtService;
import com.codertq.selleradmin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * author: coder_tq
 * date: 2024/4/15
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public SignInResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        String token = jwtService.generateToken(user);
        SignInResponse signInResponse = SignInResponse.builder()
                .accessToken(token)
                .username(user.getUsername())
                .roles(user.getRoles())
                .expires(jwtService.extractExpirationDate(token).getTime())
                .build();
        return signInResponse;
    }

    @Override
    public String signup(SignUpRequest request) {
        var user = User.builder().username(request.getUsername()).password(passwordEncoder.encode(request.getPassword())).build();
        userService.save(user);
        return jwtService.generateToken(user);
    }
}
