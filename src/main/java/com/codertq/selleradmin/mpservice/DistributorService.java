package com.codertq.selleradmin.mpservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codertq.selleradmin.domain.dao.DistributorDAO;

import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/14
 */
public interface DistributorService extends IService<DistributorDAO> {
    DistributorDAO getDistributorByUrlCode(String code);

    List<DistributorDAO> getAll();

    Boolean createDistributor(DistributorDAO distributorDAO);

    Boolean updateDistributor(DistributorDAO distributorDAO);
}
