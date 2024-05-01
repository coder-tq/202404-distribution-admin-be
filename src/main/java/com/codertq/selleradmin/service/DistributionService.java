package com.codertq.selleradmin.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.codertq.selleradmin.utils.DateTimeUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * author: coder_tq
 * date: 2024/4/26
 */
public interface DistributionService {
    /**
     * 导出所有分销数据到Excel，包含金额, 打包成zip
     */

    void exportAllDistributionDataToExcel(ZonedDateTime dateTime, HttpServletResponse response);

    void exportDataToExcel(ZonedDateTime dateTime, HttpServletResponse response);

    void exportAllDistributionDataWithPriceToExcel(ZonedDateTime date, HttpServletResponse response);
}
