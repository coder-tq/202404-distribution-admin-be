package com.codertq.selleradmin.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * author: coder_tq
 * date: 2024/4/21
 */
@Getter
@AllArgsConstructor
public enum WhetherDeleteEnum {
    DELETED(1, "已删除"),
    NOT_DELETED(0, "未删除");
    private final int code;
    private final String desc;
}
