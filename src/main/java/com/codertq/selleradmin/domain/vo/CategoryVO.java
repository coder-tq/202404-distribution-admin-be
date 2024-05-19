package com.codertq.selleradmin.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * author: coder_tq
 * date: 2024/4/20
 */
@Data
@Builder
public class CategoryVO {
    private String id;
    private String name;
    private String code;
    private String price;
    private Integer sortBy;
    private String inventory;
    private String totalInventory;
}
