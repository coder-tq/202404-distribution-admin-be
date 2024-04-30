package com.codertq.selleradmin.mpservice;

import com.codertq.selleradmin.domain.pojo.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

/**
 * author: coder_tq
 * date: 2024/4/15
 */
public interface UserService {
    UserDetailsService userDetailsService();

    Optional<User> findByUsername(String username);

    boolean save(User user);
}
