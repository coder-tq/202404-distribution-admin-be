package com.codertq.selleradmin.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * author: coder_tq
 * date: 2024/4/21
 */
@Data
@Builder
public class DistributionDetailVO {
    private String id;
    private String categoryId;
    private String categoryName;
    private String categoryCode;
    private String price;
    private String count;
}
