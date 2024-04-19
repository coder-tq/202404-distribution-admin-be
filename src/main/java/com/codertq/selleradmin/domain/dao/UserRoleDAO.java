package com.codertq.selleradmin.domain.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * author: coder_tq
 * date: 2024/4/16
 * CREATE TABLE `user_role` (
 *   `id` bigint NOT NULL AUTO_INCREMENT,
 *   `user_id` bigint NOT NULL,
 *   `role` varchar(255) DEFAULT NULL,
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
 */
@Data
@Builder
@TableName("`user_role`")
public class UserRoleDAO {
    @TableId
    private Long id;
    private Long userId;
    private String role;
}
