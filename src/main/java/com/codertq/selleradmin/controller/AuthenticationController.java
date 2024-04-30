package com.codertq.selleradmin.controller;

import com.codertq.selleradmin.domain.pojo.Result;
import com.codertq.selleradmin.domain.vo.request.SignInRequest;
import com.codertq.selleradmin.domain.vo.request.SignUpRequest;
import com.codertq.selleradmin.domain.vo.response.SignInResponse;
import com.codertq.selleradmin.mpservice.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: coder_tq
 * date: 2024/4/15
 */

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/signup")
    public Result<String> signup(@RequestBody SignUpRequest request) {
        return Result.success(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public Result<SignInResponse> signin(@RequestBody SignInRequest request) {
        try {
            return Result.success(authenticationService.signin(request));
        } catch (BadCredentialsException e) {
            return Result.fail(-1, "用户名或密码错误");
        }
    }
}
