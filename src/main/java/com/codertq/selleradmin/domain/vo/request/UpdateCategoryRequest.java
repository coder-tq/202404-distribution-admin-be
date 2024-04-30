package com.codertq.selleradmin.domain.vo.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

/**
 * author: coder_tq
 * date: 2024/4/25
 */
@Data
public class UpdateCategoryRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime date;
    private String categoryId;
    private String price;
    private String inventory;
}
