package com.codertq.selleradmin.domain.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: coder_tq
 * date: 2024/4/21
 * CREATE TABLE `distribution_detail` (
 *   `id` bigint NOT NULL AUTO_INCREMENT,
 *   `category_id` bigint NOT NULL,
 *   `count` decimal(40,10) DEFAULT NULL,
 *   `price` decimal(40,10) DEFAULT NULL,
 *   `distribution_info_id` bigint DEFAULT NULL,
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("`distribution_detail`")
public class DistributionDetailDAO {
    @TableId
    private Long id;
    private Long categoryId;
    private Double count;
    private Double price;
    private Long distributionInfoId;
}
