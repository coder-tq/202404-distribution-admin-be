package com.codertq.selleradmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.codertq.selleradmin.domain.dao.CategoryDAO;
import com.codertq.selleradmin.domain.vo.request.CreateCategoryRequest;
import com.codertq.selleradmin.mpservice.CategoryMPService;
import com.codertq.selleradmin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * author: coder_tq
 * date: 2024/5/18
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMPService categoryMPService;

    @Override
    public Boolean createCategory(CreateCategoryRequest request) {
        checkCreateCategoryRequest(request);
        CategoryDAO categoryDAO = new CategoryDAO();
        categoryDAO.setName(request.getName());
        categoryDAO.setCode(request.getCode());
        categoryDAO.setSortBy(request.getSortBy());
        return categoryMPService.save(categoryDAO);
    }

    private void checkCreateCategoryRequest(CreateCategoryRequest request) {
        LambdaQueryWrapper<CategoryDAO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryDAO::getCode,request.getCode());
        Optional.ofNullable(categoryMPService.getOne(queryWrapper)).ifPresent(categoryDAO -> {
            throw new IllegalArgumentException("类别编码已存在");
        });
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("类别名称不能为空");
        }
        if (request.getCode() == null || request.getCode().isEmpty()) {
            throw new IllegalArgumentException("类别编码不能为空");
        }
        if (request.getSortBy() == null) {
            throw new IllegalArgumentException("排序字段不能为空");
        }
    }
}
