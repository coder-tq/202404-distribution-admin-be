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
 * date: 2024/4/21
 * CREATE TABLE `distribution` (
 *   `id` bigint NOT NULL AUTO_INCREMENT,
 *   `distributor_name` varchar(255) DEFAULT NULL,
 *   `distributor_phone` varchar(255) DEFAULT NULL,
 *   `date` date DEFAULT NULL,
 *   `whether_delete` tinyint DEFAULT '0',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("`distribution`")
public class DistributionDAO {
    @TableId
    private Long id;
    private String distributorName;
    private String distributionType;
    private String distributorPhone;
    private Date date;
    private Integer sortBy;
    private Integer whetherDelete;
}
