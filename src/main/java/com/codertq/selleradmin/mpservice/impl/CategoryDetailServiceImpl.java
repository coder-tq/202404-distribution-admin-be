package com.codertq.selleradmin.mpservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codertq.selleradmin.domain.dao.CategoryDetailDAO;
import com.codertq.selleradmin.mapper.CategoryDetailMapper;
import com.codertq.selleradmin.mpservice.CategoryDetailService;
import org.springframework.stereotype.Service;

/**
 * author: coder_tq
 * date: 2024/4/21
 */
@Service
public class CategoryDetailServiceImpl extends ServiceImpl<CategoryDetailMapper, CategoryDetailDAO> implements CategoryDetailService {
}
