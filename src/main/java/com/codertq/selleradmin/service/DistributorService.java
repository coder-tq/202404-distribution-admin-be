package com.codertq.selleradmin.service;

import com.codertq.selleradmin.domain.dao.DistributorDAO;

import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/14
 */
public interface DistributorService {
    DistributorDAO getDistributorByUrlCode(String code);

    List<DistributorDAO> getAll();
}
