package com.codertq.selleradmin.domain.vo;

import com.codertq.selleradmin.domain.dao.DistributorDAO;
import lombok.Data;

/**
 * author: coder_tq
 * date: 2024/4/20
 */
@Data
public class DistributorVO {
    private String id;
    private String name;
    private String phone;
    private String urlCode;

    public static DistributorVO of(DistributorDAO distributorDAO) {
        DistributorVO distributorVO = new DistributorVO();
        distributorVO.setId(distributorDAO.getId().toString());
        distributorVO.setName(distributorDAO.getName());
        distributorVO.setPhone(distributorDAO.getPhone());
        distributorVO.setUrlCode(distributorDAO.getUrlCode());
        return distributorVO;
    }
}
