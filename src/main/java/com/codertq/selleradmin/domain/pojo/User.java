package com.codertq.selleradmin.domain.pojo;

import com.codertq.selleradmin.domain.dao.UserDAO;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/15
 */
@Data
@Builder
public class User implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private ZonedDateTime loginTime;
    private List<String> roles;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static User of(UserDAO userDAO) {
        if (userDAO == null) {
            return null;
        }
        return User.builder()
                .id(userDAO.getId())
                .username(userDAO.getUsername())
                .password(userDAO.getPassword())
                .loginTime(ZonedDateTime.now())
                .build();
    }
}
