package com.codertq.selleradmin.domain.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: coder_tq
 * date: 2024/4/20
 * CREATE TABLE `category` (
 *   `id` bigint NOT NULL AUTO_INCREMENT,
 *   `name` varchar(255) DEFAULT NULL,
 *   `code` varchar(255) DEFAULT NULL,
 *   `price` decimal(40,10) DEFAULT NULL,
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`category`")
public class CategoryDAO {
    @TableId
    private Long id;
    private String name;
    private String code;
    private Double price;
}
