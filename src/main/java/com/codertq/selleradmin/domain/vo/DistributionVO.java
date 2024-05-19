package com.codertq.selleradmin.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/21
 */
@Data
@Builder
public class DistributionVO {
    private String id;
    private String distributorName;
    private String distributionType;
    private String distributorPhone;
    private Integer distributorSortBy;
    private List<DistributionDetailVO> distributionDetailList;
}
