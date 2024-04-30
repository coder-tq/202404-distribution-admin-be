package com.codertq.selleradmin.domain.vo.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/22
 */
@Data
public class UpdateCategoryListRequest {
    private List<CategoryDetail> categoryDetailList;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime date;
    @Data
    public static class CategoryDetail {
        private String categoryId;
        private String price;
        private String inventory;
    }
}
