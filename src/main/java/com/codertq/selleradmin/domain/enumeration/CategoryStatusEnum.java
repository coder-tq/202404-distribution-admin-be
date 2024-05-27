package com.codertq.selleradmin.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * author: coder_tq
 * date: 2024/5/22
 */
@Getter
@AllArgsConstructor
public enum CategoryStatusEnum {
    ENABLED(0, "启用中"),
    DISABLED(1, "已禁用"),
    DELETED(2, "已删除");

    private final int code;
    private final String desc;
}
