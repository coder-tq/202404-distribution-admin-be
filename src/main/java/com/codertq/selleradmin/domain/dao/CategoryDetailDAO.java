package com.codertq.selleradmin.domain.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * author: coder_tq
 * date: 2024/4/20
 * CREATE TABLE `category_detail` (
 *   `id` bigint NOT NULL AUTO_INCREMENT,
 *   `category_id` bigint NOT NULL,
 *   `price` decimal(40,10) DEFAULT NULL COMMENT '当日价格',
 *   `inventory` int DEFAULT NULL COMMENT '库存',
 *   `date` date DEFAULT NULL COMMENT '日期',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("`category_detail`")
public class CategoryDetailDAO {
    @TableId
    private Long id;
    private Long categoryId;
    private Double price;
    private Double inventory;
    private Date date;
}
