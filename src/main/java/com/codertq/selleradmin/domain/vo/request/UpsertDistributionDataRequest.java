package com.codertq.selleradmin.domain.vo.request;

import com.codertq.selleradmin.domain.vo.DistributionDetailVO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/22
 */
@Data
public class UpsertDistributionDataRequest {
    private String id;
    private String distributorName;
    private String distributionType;
    private String distributorPhone;
    private Integer distributorSortBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;
    private List<DistributionDetailVO> distributionDetailList;
}
