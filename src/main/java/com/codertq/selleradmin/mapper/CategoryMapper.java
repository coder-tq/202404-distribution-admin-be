package com.codertq.selleradmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codertq.selleradmin.domain.dao.CategoryDAO;
import com.codertq.selleradmin.domain.dao.CategoryDetailDAO;
import org.apache.ibatis.annotations.Mapper;

/**
 * author: coder_tq
 * date: 2024/4/21
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryDAO> {
}
