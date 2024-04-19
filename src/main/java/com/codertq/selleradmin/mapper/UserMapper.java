package com.codertq.selleradmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codertq.selleradmin.domain.dao.UserDAO;
import org.apache.ibatis.annotations.Mapper;

/**
 * author: coder_tq
 * date: 2024/4/15
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDAO> {
}
