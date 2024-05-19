package com.codertq.selleradmin.domain.vo.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

/**
 * author: coder_tq
 * date: 2024/5/18
 */
@Data
public class CreateCategoryRequest {
    private String name;
    private String code;
    private Integer sortBy;
}
