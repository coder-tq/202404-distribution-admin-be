package com.codertq.selleradmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codertq.selleradmin.domain.dao.DistributorDAO;
import com.codertq.selleradmin.mapper.DistributorMapper;
import com.codertq.selleradmin.service.DistributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/14
 */
@Service
public class DistributorServiceImpl implements DistributorService {

    @Autowired
    private DistributorMapper distributorMapper;

    @Override
    public DistributorDAO getDistributorByUrlCode(String code) {
        QueryWrapper<DistributorDAO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url_code", code);
        return distributorMapper.selectOne(queryWrapper);
    }

    @Override
    public List<DistributorDAO> getAll() {
        return distributorMapper.selectList(null);
    }
}
