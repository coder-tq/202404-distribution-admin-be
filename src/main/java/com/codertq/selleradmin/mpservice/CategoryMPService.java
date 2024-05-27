package com.codertq.selleradmin.mpservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codertq.selleradmin.domain.dao.CategoryDAO;
import com.codertq.selleradmin.domain.enumeration.CategoryStatusEnum;
import com.codertq.selleradmin.domain.vo.CategoryVO;
import com.codertq.selleradmin.domain.vo.request.UpdateCategoryListRequest;
import com.codertq.selleradmin.domain.vo.request.UpdateCategoryRequest;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/21
 */
public interface CategoryMPService extends IService<CategoryDAO> {
    List<CategoryVO> getCurrentCategoryList(ZonedDateTime date);

    Boolean updateCategoryList(UpdateCategoryListRequest request);

    Boolean updateCategory(UpdateCategoryRequest request);

    Boolean updateCategoryStatusById(String id, CategoryStatusEnum categoryStatusEnum);
}
