package com.codertq.selleradmin.mpservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codertq.selleradmin.domain.dao.CategoryDAO;
import com.codertq.selleradmin.domain.dao.CategoryDetailDAO;
import com.codertq.selleradmin.domain.dao.DistributionDAO;
import com.codertq.selleradmin.domain.dao.DistributionDetailDAO;
import com.codertq.selleradmin.domain.enumeration.WhetherDeleteEnum;
import com.codertq.selleradmin.domain.vo.CategoryVO;
import com.codertq.selleradmin.domain.vo.request.UpdateCategoryListRequest;
import com.codertq.selleradmin.domain.vo.request.UpdateCategoryRequest;
import com.codertq.selleradmin.mapper.CategoryDetailMapper;
import com.codertq.selleradmin.mapper.CategoryMapper;
import com.codertq.selleradmin.mapper.DistributionDetailMapper;
import com.codertq.selleradmin.mapper.DistributionMapper;
import com.codertq.selleradmin.mpservice.CategoryDetailService;
import com.codertq.selleradmin.mpservice.CategoryMPService;
import com.codertq.selleradmin.utils.DateTimeUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * author: coder_tq
 * date: 2024/4/21
 */
@Service
public class CategoryMPServiceImpl extends ServiceImpl<CategoryMapper, CategoryDAO> implements CategoryMPService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryDetailMapper categoryDetailMapper;
    @Autowired
    private CategoryDetailService categoryDetailService;
    @Autowired
    private DistributionDetailMapper distributionDetailMapper;
    @Autowired
    private DistributionMapper distributionMapper;
    @Override
    public List<CategoryVO> getCurrentCategoryList(ZonedDateTime date) {
        DecimalFormat df = new DecimalFormat("#.##");
        LocalDate localDate = DateTimeUtil.getLocalDate(date);
        QueryWrapper<CategoryDAO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort_by");
        List<CategoryDAO> categoryDAOS = categoryMapper.selectList(queryWrapper);
        QueryWrapper<CategoryDetailDAO> categoryDetailDAOQueryWrapper = new QueryWrapper<>();
        categoryDetailDAOQueryWrapper.in("category_id", categoryDAOS.stream().map(CategoryDAO::getId).toList());
        categoryDetailDAOQueryWrapper.eq("date", localDate);
        List<CategoryDetailDAO> categoryDetailDAOS = categoryDetailMapper.selectList(categoryDetailDAOQueryWrapper);


        // 查询配货数据
        QueryWrapper<DistributionDAO> distributionDAOQueryWrapper = new QueryWrapper<>();
        distributionDAOQueryWrapper.eq("date", Date.valueOf(DateTimeUtil.getLocalDate(date)));
        distributionDAOQueryWrapper.eq("whether_delete", WhetherDeleteEnum.NOT_DELETED);
        List<Long> list = distributionMapper.selectList(distributionDAOQueryWrapper).stream().map(DistributionDAO::getId).toList();


        List<DistributionDetailDAO> distributionDetailDAOList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)) {
            QueryWrapper<DistributionDetailDAO> distributionDetailDAOQueryWrapper = new QueryWrapper<>();
            distributionDetailDAOQueryWrapper.in("distribution_info_id", list);
            distributionDetailDAOList = distributionDetailMapper.selectList(distributionDetailDAOQueryWrapper);
        }

        List<DistributionDetailDAO> finalDistributionDetailDAOList = distributionDetailDAOList;
        return categoryDAOS.stream().map(categoryDAO -> {
            CategoryDetailDAO categoryDetailDAO = categoryDetailDAOS.stream()
                    .filter(detail -> detail.getCategoryId().equals(categoryDAO.getId())).findFirst()
                    .orElse(new CategoryDetailDAO(null, categoryDAO.getId(), 0.0, 0.0, Date.valueOf(DateTimeUtil.getLocalDate(date))));
            Double totalInventory = categoryDetailDAO.getInventory();
            for (DistributionDetailDAO distributionDetailDAO : finalDistributionDetailDAOList) {
                if (Objects.equals(distributionDetailDAO.getCategoryId(), categoryDAO.getId())) {
                    categoryDetailDAO.setInventory(categoryDetailDAO.getInventory() - distributionDetailDAO.getCount());
                }
            }
            return CategoryVO.builder()
                    .id(String.valueOf(categoryDAO.getId()))
                    .code(categoryDAO.getCode())
                    .name(categoryDAO.getName())
                    .price(df.format(categoryDetailDAO.getPrice()))
                    .inventory(df.format(categoryDetailDAO.getInventory()))
                    .totalInventory(df.format(totalInventory))
                    .build();
        }).toList();
    }

    @Override
    @Transactional
    public Boolean updateCategoryList(UpdateCategoryListRequest request) {
        QueryWrapper<CategoryDetailDAO> categoryDetailDAOQueryWrapper = new QueryWrapper<>();
        categoryDetailDAOQueryWrapper.eq("date", Date.valueOf(DateTimeUtil.getLocalDate(request.getDate())));
        categoryDetailMapper.delete(categoryDetailDAOQueryWrapper);
        List<CategoryDetailDAO> categoryDetailDAOS = request.getCategoryDetailList().stream().map(categoryVO -> {
            CategoryDetailDAO categoryDetailDAO = new CategoryDetailDAO();
            categoryDetailDAO.setCategoryId(Long.parseLong(categoryVO.getCategoryId()));
            categoryDetailDAO.setPrice(Double.parseDouble(categoryVO.getPrice()));
            categoryDetailDAO.setInventory(Double.parseDouble(categoryVO.getInventory()));
            categoryDetailDAO.setDate(Date.valueOf(DateTimeUtil.getLocalDate(request.getDate())));
            return categoryDetailDAO;
        }).toList();
        categoryDetailService.saveBatch(categoryDetailDAOS);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateCategory(UpdateCategoryRequest request) {
        QueryWrapper<CategoryDetailDAO> categoryDetailDAOQueryWrapper = new QueryWrapper<>();
        categoryDetailDAOQueryWrapper.eq("category_id", request.getCategoryId());
        categoryDetailDAOQueryWrapper.eq("date", Date.valueOf(DateTimeUtil.getLocalDate(request.getDate())));
        CategoryDetailDAO categoryDetailDAO = categoryDetailMapper.selectOne(categoryDetailDAOQueryWrapper);
        if (categoryDetailDAO == null) {
            categoryDetailMapper.insert(CategoryDetailDAO.builder()
                    .categoryId(Long.parseLong(request.getCategoryId()))
                    .price(Double.parseDouble(request.getPrice()))
                    .inventory(Double.parseDouble(request.getInventory()))
                    .date(Date.valueOf(DateTimeUtil.getLocalDate(request.getDate())))
                    .build());
        } else {
            UpdateWrapper<CategoryDetailDAO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("category_id", request.getCategoryId());
            updateWrapper.eq("date", Date.valueOf(DateTimeUtil.getLocalDate(request.getDate())));
            updateWrapper.set("price", Double.parseDouble(request.getPrice()));
            updateWrapper.set("inventory", Double.parseDouble(request.getInventory()));
            categoryDetailMapper.update(updateWrapper);
        }
        return true;
    }
}
