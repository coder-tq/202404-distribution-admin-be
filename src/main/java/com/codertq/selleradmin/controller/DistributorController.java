package com.codertq.selleradmin.controller;

import com.codertq.selleradmin.domain.dao.DistributorDAO;
import com.codertq.selleradmin.domain.pojo.Result;
import com.codertq.selleradmin.service.DistributorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/14
 */
@RestController
@RequestMapping("/api/v1/distributor")
@Tag(name = "DistributorController", description = "分销商接口")
public class DistributorController {

    @Autowired
    private DistributorService distributorService;

    @Operation(summary = "根据urlCode获取分销商信息")
    @GetMapping("/getByUrlCode")
    public Result<DistributorDAO> getByUrlCode(String code) {
        return Result.success(distributorService.getDistributorByUrlCode(code));
    }

    @Operation(summary = "获取所有分销商信息")
    @GetMapping("/getAll")
    public Result<List<DistributorDAO>> getAll() {
        return Result.success(distributorService.getAll());
    }
}
