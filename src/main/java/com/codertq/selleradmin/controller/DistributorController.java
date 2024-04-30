package com.codertq.selleradmin.controller;

import com.codertq.selleradmin.domain.dao.DistributorDAO;
import com.codertq.selleradmin.domain.pojo.Result;
import com.codertq.selleradmin.domain.vo.DistributorVO;
import com.codertq.selleradmin.mpservice.DistributorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result<DistributorVO> getByUrlCode(String code) {
        DistributorDAO distributorByUrlCode = distributorService.getDistributorByUrlCode(code);
        return Result.success(DistributorVO.of(distributorByUrlCode));
    }

    @Operation(summary = "获取所有分销商信息")
    @GetMapping("/getAll")
    public Result<List<DistributorVO>> getAll() {
        List<DistributorDAO> all = distributorService.getAll();
        return Result.success(all.stream().map(DistributorVO::of).toList());
    }


    @Operation(summary = "新增分销商信息")
    @PostMapping("/create")
    public Result<Boolean> createDistributor(@RequestBody DistributorVO distributorVO) {
        DistributorDAO distributorDAO = new DistributorDAO();
        BeanUtils.copyProperties(distributorVO, distributorDAO);
        distributorDAO.setId(Long.valueOf(distributorVO.getId()));
        return Result.success(distributorService.createDistributor(distributorDAO));
    }


    @Operation(summary = "新增分销商信息")
    @PostMapping("/update")
    public Result<Boolean> updateDistributor(@RequestBody DistributorVO distributorVO) {
        DistributorDAO distributorDAO = new DistributorDAO();
        BeanUtils.copyProperties(distributorVO, distributorDAO);
        distributorDAO.setId(Long.valueOf(distributorVO.getId()));
        return Result.success(distributorService.updateDistributor(distributorDAO));
    }
}
