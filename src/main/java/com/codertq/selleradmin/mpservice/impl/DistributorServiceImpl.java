package com.codertq.selleradmin.mpservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codertq.selleradmin.domain.dao.DistributorDAO;
import com.codertq.selleradmin.mapper.DistributorMapper;
import com.codertq.selleradmin.mpservice.DistributorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * author: coder_tq
 * date: 2024/4/14
 */
@Service
public class DistributorServiceImpl extends ServiceImpl<DistributorMapper, DistributorDAO> implements DistributorService {

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

    @Override
    public Boolean createDistributor(DistributorDAO distributorDAO) {
        checkDistributor(distributorDAO);
        return distributorMapper.insert(distributorDAO) > 0;
    }

    @Override
    public Boolean updateDistributor(DistributorDAO distributorDAO) {
        checkDistributor(distributorDAO);
        QueryWrapper<DistributorDAO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", distributorDAO.getId());
        return distributorMapper.update(distributorDAO, queryWrapper) > 0;
    }


    private void checkDistributor(DistributorDAO distributorDAO) {
        if (distributorDAO == null) {
            throw new RuntimeException("经销商不能为空");
        }
        if (StringUtils.isEmpty(distributorDAO.getPhone()) || StringUtils.isEmpty(distributorDAO.getUrlCode())) {
            throw new RuntimeException("手机号或者短链接不能为空");
        }
        QueryWrapper<DistributorDAO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", distributorDAO.getPhone()).or().eq("url_code", distributorDAO.getUrlCode());
        List<DistributorDAO> distributorDAOS = baseMapper.selectList(queryWrapper);
        if (distributorDAO.getId() != null) {
            Optional<DistributorDAO> any = distributorDAOS.stream().filter((dao) -> !distributorDAO.getId().equals(dao.getId())).findAny();
            if (any.isPresent()) {
                throw new RuntimeException("手机号或者短链接已经存在");
            }
        } else {
            if (!distributorDAOS.isEmpty()) {
                throw new RuntimeException("手机号或者短链接已经存在");
            }
        }

    }
}
