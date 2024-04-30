package com.codertq.selleradmin.mpservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codertq.selleradmin.domain.dao.CategoryDAO;
import com.codertq.selleradmin.domain.dao.DistributionDAO;
import com.codertq.selleradmin.domain.dao.DistributionDetailDAO;
import com.codertq.selleradmin.domain.enumeration.WhetherDeleteEnum;
import com.codertq.selleradmin.domain.vo.DistributionDetailVO;
import com.codertq.selleradmin.domain.vo.DistributionVO;
import com.codertq.selleradmin.domain.vo.request.UpsertDistributionDataRequest;
import com.codertq.selleradmin.mapper.DistributionMapper;
import com.codertq.selleradmin.mpservice.CategoryMPService;
import com.codertq.selleradmin.mpservice.DistributionDetailService;
import com.codertq.selleradmin.mpservice.DistributionMPService;
import com.codertq.selleradmin.utils.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/21
 */
@Service
public class DistributionMPServiceImpl extends ServiceImpl<DistributionMapper, DistributionDAO> implements DistributionMPService {
    @Autowired
    private DistributionDetailService distributionDetailService;
    @Autowired
    private CategoryMPService categoryMPService;
    @Autowired
    private DistributionMapper distributionMapper;

    @Override
    public List<DistributionVO> getCurrentDistributionList(ZonedDateTime date) {
        QueryWrapper<DistributionDAO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date", DateTimeUtil.getLocalDate(date));
        queryWrapper.eq("whether_delete", WhetherDeleteEnum.NOT_DELETED.getCode());
        queryWrapper.orderBy(true, true, "distributor_name");
        List<DistributionDAO> list = this.list(queryWrapper);
        return buildDistributionVOList(list);
    }

    @Override
    @Transactional
    public Boolean upsertDistributionData(UpsertDistributionDataRequest request) {
        if (StringUtils.isBlank(request.getId())) {
            return createDistribution(request);
        } else {
            return updateDistribution(request);
        }
    }

    @Override
    public Boolean deleteDistributionData(String id) {
        UpdateWrapper<DistributionDAO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("whether_delete", WhetherDeleteEnum.DELETED.getCode());
        updateWrapper.eq("id", id);
        distributionMapper.update(updateWrapper);
        return true;
    }

    @Override
    public List<DistributionVO> getCurrentDistributionListByType(ZonedDateTime date, String distributionType) {
        QueryWrapper<DistributionDAO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(distributionType)) {
            queryWrapper.eq("distribution_type", distributionType);
        }
        queryWrapper.eq("date", DateTimeUtil.getLocalDate(date));
        queryWrapper.eq("whether_delete", WhetherDeleteEnum.NOT_DELETED.getCode());
        queryWrapper.orderBy(true, true, "distributor_name");
        List<DistributionDAO> list = this.list(queryWrapper);
        return buildDistributionVOList(list);
    }

    protected Boolean createDistribution(UpsertDistributionDataRequest request) {
        DistributionDAO distribution = DistributionDAO.builder()
                .distributorName(request.getDistributorName())
                .distributionType(request.getDistributionType())
                .distributorPhone(request.getDistributorPhone())
                .date(request.getDate())
                .whetherDelete(WhetherDeleteEnum.NOT_DELETED.getCode())
                .build();
        distributionMapper.insert(distribution);

        List<DistributionDetailDAO> distributionDetailDAOList = new ArrayList<>();
        request.getDistributionDetailList().forEach((distributionDetailVO) -> {
            Long categoryId;
            if (StringUtils.isBlank(distributionDetailVO.getCategoryId())) {
                QueryWrapper<CategoryDAO> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("code", distributionDetailVO.getCategoryCode());
                categoryId = categoryMPService.getOne(queryWrapper).getId();
            } else {
                categoryId = Long.parseLong(distributionDetailVO.getCategoryId());
            }
            DistributionDetailDAO distributionDetailDAO = DistributionDetailDAO.builder()
                    .distributionInfoId(distribution.getId())
                    .categoryId(categoryId)
                    .count(Double.parseDouble(distributionDetailVO.getCount()))
                    .price(Double.parseDouble(distributionDetailVO.getPrice()))
                    .build();
            distributionDetailDAOList.add(distributionDetailDAO);
        });
        distributionDetailService.saveBatch(distributionDetailDAOList);
        return true;
    }

    private Boolean updateDistribution(UpsertDistributionDataRequest request) {
        DistributionDAO distributionDAO = distributionMapper.selectById(request.getId());
        if (distributionDAO == null) {
            return false;
        }
        UpdateWrapper<DistributionDAO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("whether_delete", WhetherDeleteEnum.DELETED.getCode());
        updateWrapper.eq("id", request.getId());
        distributionMapper.update(updateWrapper);
        DistributionDAO distribution = DistributionDAO.builder()
            .distributorName(request.getDistributorName())
            .distributionType(request.getDistributionType())
            .distributorPhone(request.getDistributorPhone())
            .date(request.getDate())
            .whetherDelete(WhetherDeleteEnum.NOT_DELETED.getCode())
            .build();
        distributionMapper.insert(distribution);

        List<DistributionDetailDAO> distributionDetailDAOList = new ArrayList<>();
        request.getDistributionDetailList().forEach((distributionDetailVO) -> {
            Long categoryId;
            if (StringUtils.isBlank(distributionDetailVO.getCategoryId())) {
                QueryWrapper<CategoryDAO> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("code", distributionDetailVO.getCategoryCode());
                categoryId = categoryMPService.getOne(queryWrapper).getId();
            } else {
                categoryId = Long.parseLong(distributionDetailVO.getCategoryId());
            }
            DistributionDetailDAO distributionDetailDAO = DistributionDetailDAO.builder()
                    .distributionInfoId(distribution.getId())
                    .categoryId(categoryId)
                    .count(Double.parseDouble(distributionDetailVO.getCount()))
                    .price(Double.parseDouble(distributionDetailVO.getPrice()))
                    .build();
            distributionDetailDAOList.add(distributionDetailDAO);
        });
        distributionDetailService.saveBatch(distributionDetailDAOList);
        return true;
    }

    private List<DistributionVO> buildDistributionVOList(List<DistributionDAO> list) {
        return list.stream().map((distributionDAO) -> {
            QueryWrapper<DistributionDetailDAO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("distribution_info_id", distributionDAO.getId());
            List<DistributionDetailDAO> distributionDetailDAOList = distributionDetailService.list(queryWrapper);
            return DistributionVO.builder()
                    .id(distributionDAO.getId().toString())
                    .distributorName(distributionDAO.getDistributorName())
                    .distributionType(distributionDAO.getDistributionType())
                    .distributorPhone(distributionDAO.getDistributorPhone())
                    .distributionDetailList(buildDistributionDetailVOList(distributionDetailDAOList))
                    .build();
        }).toList();
    }

    private List<DistributionDetailVO> buildDistributionDetailVOList(List<DistributionDetailDAO> distributionDetailDAOList) {
        return distributionDetailDAOList.stream().map((distributionDetailDAO) -> {
            QueryWrapper<CategoryDAO> categoryDAOQueryWrapper = new QueryWrapper<>();
            categoryDAOQueryWrapper.eq("id", distributionDetailDAO.getCategoryId());
            CategoryDAO categoryDAO = categoryMPService.getOne(categoryDAOQueryWrapper);
            return DistributionDetailVO.builder()
                    .id(distributionDetailDAO.getId().toString())
                    .categoryId(distributionDetailDAO.getCategoryId().toString())
                    .categoryName(categoryDAO.getName())
                    .categoryCode(categoryDAO.getCode())
                    .count(distributionDetailDAO.getCount().toString())
                    .price(distributionDetailDAO.getPrice().toString())
                    .build();
        }).toList();
    }


}
