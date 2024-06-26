package com.codertq.selleradmin.domain.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: coder_tq
 * date: 2024/4/14
 * CREATE TABLE `distributor` (
 *   `id` bigint NOT NULL AUTO_INCREMENT,
 *   `name` varchar(255) DEFAULT NULL,
 *   `phone` varchar(255) DEFAULT NULL,
 *   `url_code` varchar(255) DEFAULT NULL COMMENT '短链接',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`distributor`")
public class DistributorDAO {

    @TableId
    private Long id;
    private String name;
    private String phone;
    private String urlCode;
}
