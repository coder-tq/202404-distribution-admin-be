package com.codertq.selleradmin.mpservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codertq.selleradmin.domain.dao.UserRoleDAO;
import com.codertq.selleradmin.mapper.UserRoleMapper;
import com.codertq.selleradmin.mpservice.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * author: coder_tq
 * date: 2024/4/16
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleDAO> implements UserRoleService {
}
