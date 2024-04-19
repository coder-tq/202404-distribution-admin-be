package com.codertq.selleradmin.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
    private String accessToken;
    private String refreshToken;
    private String username;
    private List<String> roles;
    private Long expires;
}
