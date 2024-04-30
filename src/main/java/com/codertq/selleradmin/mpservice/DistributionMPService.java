package com.codertq.selleradmin.mpservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codertq.selleradmin.domain.dao.DistributionDAO;
import com.codertq.selleradmin.domain.vo.DistributionVO;
import com.codertq.selleradmin.domain.vo.request.UpsertDistributionDataRequest;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/21
 */
public interface DistributionMPService extends IService<DistributionDAO> {
    List<DistributionVO> getCurrentDistributionList(ZonedDateTime date);

    Boolean upsertDistributionData(UpsertDistributionDataRequest request);

    Boolean deleteDistributionData(String id);

    List<DistributionVO> getCurrentDistributionListByType(ZonedDateTime date, String distributionType);
}
