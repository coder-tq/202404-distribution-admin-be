package com.codertq.selleradmin.controller;

import com.codertq.selleradmin.domain.pojo.Result;
import com.codertq.selleradmin.domain.vo.DistributionVO;
import com.codertq.selleradmin.domain.vo.request.UpsertDistributionDataRequest;
import com.codertq.selleradmin.mpservice.DistributionMPService;
import com.codertq.selleradmin.service.DistributionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/20
 */
@RestController
@RequestMapping("/api/v1/distribution")
@Tag(name = "DistributionController", description = "分销数据接口")
public class DistributionController {

    @Autowired
    private DistributionMPService distributionMPService;
    @Autowired
    private DistributionService distributionService;

    @GetMapping("/queryByDate")
    @Operation(summary = "根据时间查询当天分销数据信息")
    public Result<List<DistributionVO>> getCurrentDistributionList(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("date") ZonedDateTime date) {
        return Result.success(distributionMPService.getCurrentDistributionList(date));
    }

    @GetMapping("/queryByDateAndType")
    @Operation(summary = "根据时间查询当天分销数据信息")
    public Result<List<DistributionVO>> getCurrentDistributionList(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("date") ZonedDateTime date,
            @RequestParam(value = "distributionType", required = false) String distributionType) {
        return Result.success(distributionMPService.getCurrentDistributionListByType(date, distributionType));
    }

    @PostMapping("/upsert")
    @Operation(summary = "更新、新增分销数据")
    public Result<Boolean> upsertDistributionData(@RequestBody UpsertDistributionDataRequest request) {
        return Result.success(distributionMPService.upsertDistributionData(request));
    }


    @PostMapping("/delete")
    @Operation(summary = "删除分销数据")
    public Result<Boolean> deleteDistributionData(String id) {
        return Result.success(distributionMPService.deleteDistributionData(id));
    }



    @GetMapping("/export")
    @Operation(summary = "导出分销数据")
    public void exportDistributionData(HttpServletResponse response) {
        distributionService.exportDataToExcel(response);
    }

    @GetMapping("/exportAllByDate")
    @Operation(summary = "导出分销数据")
    public void exportAllDistributionData(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("date") ZonedDateTime date, HttpServletResponse response) {
        distributionService.exportAllDistributionDataToExcel(date, response);
    }
}
