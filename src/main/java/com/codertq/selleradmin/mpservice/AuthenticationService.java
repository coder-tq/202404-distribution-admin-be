package com.codertq.selleradmin.mpservice;

import com.codertq.selleradmin.domain.vo.request.SignInRequest;
import com.codertq.selleradmin.domain.vo.request.SignUpRequest;
import com.codertq.selleradmin.domain.vo.response.SignInResponse;

/**
 * author: coder_tq
 * date: 2024/4/15
 */
public interface AuthenticationService {
    SignInResponse signin(SignInRequest request);

    String signup(SignUpRequest request);
}
