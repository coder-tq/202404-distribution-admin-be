package com.codertq.selleradmin.domain.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * author: coder_tq
 * date: 2024/4/15
 * CREATE TABLE `user` (
 *   `id` bigint NOT NULL AUTO_INCREMENT,
 *   `username` varchar(255) DEFAULT NULL,
 *   `password` varchar(255) DEFAULT NULL,
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
 */
@Data
@Builder
@TableName("`user`")
public class UserDAO {
    @TableId
    private Long id;
    private String username;
    private String password;

}
