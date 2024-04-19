package com.codertq.selleradmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codertq.selleradmin.domain.dao.UserDAO;
import com.codertq.selleradmin.domain.dao.UserRoleDAO;
import com.codertq.selleradmin.domain.pojo.User;
import com.codertq.selleradmin.mapper.UserMapper;
import com.codertq.selleradmin.mapper.UserRoleMapper;
import com.codertq.selleradmin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * author: coder_tq
 * date: 2024/4/15
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> this.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        QueryWrapper<UserDAO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UserDAO userDAO = userMapper.selectOne(queryWrapper);
        if (userDAO == null) {
            return Optional.empty();
        }
        User user = User.of(userDAO);
        QueryWrapper<UserRoleDAO> userRoleDAOQueryWrapper = new QueryWrapper<>();
        userRoleDAOQueryWrapper.eq("user_id", user.getId());
        List<UserRoleDAO> userRoleDAOS = userRoleMapper.selectList(userRoleDAOQueryWrapper);
        user.setRoles(userRoleDAOS.stream().map(UserRoleDAO::getRole).toList());
        return Optional.of(user);
    }

    @Override
    public boolean save(User user) {
        UserDAO userDAO = UserDAO.builder().username(user.getUsername()).password(user.getPassword()).build();
        return userMapper.insert(userDAO) > 0;
    }
}
