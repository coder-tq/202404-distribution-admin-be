package com.codertq.selleradmin.service;

import com.codertq.selleradmin.domain.vo.request.CreateCategoryRequest;

/**
 * author: coder_tq
 * date: 2024/5/18
 */
public interface CategoryService {
    Boolean createCategory(CreateCategoryRequest request);
}
