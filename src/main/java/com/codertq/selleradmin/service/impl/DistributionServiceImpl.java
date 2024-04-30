package com.codertq.selleradmin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.codertq.selleradmin.domain.vo.CategoryVO;
import com.codertq.selleradmin.domain.vo.DistributionDetailVO;
import com.codertq.selleradmin.domain.vo.DistributionVO;
import com.codertq.selleradmin.mpservice.CategoryMPService;
import com.codertq.selleradmin.mpservice.DistributionMPService;
import com.codertq.selleradmin.service.DistributionService;
import com.codertq.selleradmin.utils.DateTimeUtil;
import com.codertq.selleradmin.utils.excel.MergeCellWriteHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * author: coder_tq
 * date: 2024/4/26
 */
@Service
public class DistributionServiceImpl implements DistributionService {

    @Autowired
    private DistributionMPService distributionMPService;

    @Autowired
    private CategoryMPService categoryMPService;

    @Override
    public void exportAllDistributionDataToExcel(ZonedDateTime dateTime, HttpServletResponse response) {
        // Prepare the response
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=distributions.zip");

        String tempDir = System.getProperty("java.io.tmpdir");
        List<File> files = new ArrayList<>();


        ClassPathResource classPathResource = new ClassPathResource("templates/exportAllDistributionDataTemplate.xlsx");
        File templateFile = null;
        try {
            templateFile = classPathResource.getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<DistributionVO> currentDistributionList = distributionMPService.getCurrentDistributionList(dateTime);
        for (DistributionVO distributionVO : currentDistributionList) {
            File tempFile = null;
            try {
                tempFile = File.createTempFile(distributionVO.getDistributorName(), ".xlsx", new File(tempDir));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            files.add(tempFile);
            try (OutputStream out = new FileOutputStream(tempFile)) {
                List<MergeCellWriteHandler.MergeCellConfig> mergeCellConfigs = new ArrayList<>();
                mergeCellConfigs.add(MergeCellWriteHandler.MergeCellConfig.builder().startRowIndex(6).mergeColumnIndex(0).mergeColumnSize(2).build());
                mergeCellConfigs.add(MergeCellWriteHandler.MergeCellConfig.builder().startRowIndex(6).mergeColumnIndex(3).mergeColumnSize(2).build());
                ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(templateFile).registerWriteHandler(new MergeCellWriteHandler(mergeCellConfigs)).excelType(ExcelTypeEnum.XLSX).build();
                List<ExportDistributionVO> list = new ArrayList<>();
                for (DistributionDetailVO distributionDetailVO : distributionVO.getDistributionDetailList()) {
                    ExportDistributionVO exportDistributionVO = new ExportDistributionVO();
                    exportDistributionVO.setDistributionCategoryName(distributionDetailVO.getCategoryName());
                    exportDistributionVO.setDistributionCategoryPrice(distributionDetailVO.getPrice());
                    exportDistributionVO.setDistributionCount(distributionDetailVO.getCount());
                    exportDistributionVO.setDistributionPrice(String.valueOf(Double.parseDouble(distributionDetailVO.getCount()) * Double.parseDouble(distributionDetailVO.getPrice())));
                    list.add(exportDistributionVO);
                }
                WriteSheet writeSheet = EasyExcel.writerSheet().build();
                // 这里注意 入参用了forceNewRow 代表在写入list的时候不管list下面有没有空行 都会创建一行，然后下面的数据往后移动。默认 是false，会直接使用下一行，如果没有则创建。
                // forceNewRow 如果设置了true,有个缺点 就是他会把所有的数据都放到内存了，所以慎用
                // 简单的说 如果你的模板有list,且list不是最后一行，下面还有数据需要填充 就必须设置 forceNewRow=true 但是这个就会把所有数据放到内存 会很耗内存
                // 如果数据量大 list不是最后一行 参照下一个
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
                excelWriter.fill(list, fillConfig, writeSheet);
                Map<String, Object> map = MapUtils.newHashMap();
                map.put("distributorName", "张三");
                LocalDate localDate = DateTimeUtil.getLocalDate(dateTime);
                map.put("year", localDate.getYear());
                map.put("month", localDate.getMonth().getValue());
                map.put("day", localDate.getDayOfMonth());
                map.put("totalPrice", 1000);
                excelWriter.fill(map, writeSheet);
                excelWriter.finish();
            } catch (IOException e) {
                // Handle the exception
            }
        }
        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            for (File file : files) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOut.putNextEntry(zipEntry);
                Files.copy(file.toPath(), zipOut);
                zipOut.closeEntry();
                file.delete();  // Delete the temporary file
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exportDataToExcel(HttpServletResponse response) {
        // Prepare the response
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=distribution.xlsx");

        try (OutputStream outputStream = response.getOutputStream()) {
            ExcelWriter excelWriter = EasyExcel.write(outputStream).excelType(ExcelTypeEnum.XLSX).build();

            List<DistributionVO> currentDistributionList = distributionMPService.getCurrentDistributionList(ZonedDateTime.now());


            Map<String, List<DistributionVO>> collect = currentDistributionList.stream().collect(HashMap::new, (map, distributionVO) -> map.computeIfAbsent(distributionVO.getDistributionType(), k -> new ArrayList<>()).add(distributionVO), HashMap::putAll);
            ;
            for (Map.Entry<String, List<DistributionVO>> entry : collect.entrySet()) {

                List<CategoryVO> currentCategoryList = categoryMPService.getCurrentCategoryList(ZonedDateTime.now());
                Set<String> removedCategory = new HashSet<>();
                Map<String, Double> totalCount = getTotalCount(currentCategoryList, entry.getValue());
                Map<String, Double> finalTotalCount = totalCount;
                currentCategoryList = currentCategoryList.stream().filter(categoryVO -> finalTotalCount.get(categoryVO.getCode()) != 0).collect(Collectors.toList());
                totalCount = getTotalCount(currentCategoryList, entry.getValue());

                List<List<String>> data = new ArrayList<>();
                for (DistributionVO distributionVO : entry.getValue()) {
                    List<String> header = new ArrayList<>();
                    List<String> count = new ArrayList<>();
                    header.add("买家\\品类名称");
                    count.add(distributionVO.getDistributorName());
                    for (CategoryVO categoryVO : currentCategoryList) {
                        for (DistributionDetailVO distributionDetailVO : distributionVO.getDistributionDetailList()) {
                            if (distributionDetailVO.getCategoryId().equals(categoryVO.getId())) {
                                if (Double.parseDouble(distributionDetailVO.getCount()) > 0) {
                                    header.add(distributionDetailVO.getCategoryName());
                                    count.add(distributionDetailVO.getCount());
                                    break;
                                }
                            }
                        }
                    }
                    data.add(header);
                    data.add(count);
                    data.add(new ArrayList<>());
                }

                List<String> categoryRow = new ArrayList<>();
                categoryRow.add("");
                List<String> sum = new ArrayList<>();
                sum.add("合计");
                for (CategoryVO categoryVO : currentCategoryList) {
                    categoryRow.add(categoryVO.getName());
                    sum.add(String.valueOf(totalCount.get(categoryVO.getCode())));
                }
                data.add(categoryRow);
                data.add(sum);
                WriteSheet writeSheet = EasyExcel.writerSheet(entry.getKey()).build();
                excelWriter.write(data, writeSheet);
            }
            excelWriter.finish();
        } catch (IOException e) {
            // Handle the exception
        }
    }

    private static Map<String, Double> getTotalCount(List<CategoryVO> currentCategoryList, List<DistributionVO> currentDistributionList) {
        Map<String, Double> totalCount = new HashMap<>();
        for (CategoryVO categoryVO : currentCategoryList) {
            double sum = 0;
            for (DistributionVO distributionVO : currentDistributionList) {
                for (DistributionDetailVO distributionDetailVO : distributionVO.getDistributionDetailList()) {
                    if (distributionDetailVO.getCategoryId().equals(categoryVO.getId())) {
                        sum += Double.parseDouble(distributionDetailVO.getCount());
                    }
                }
            }
            totalCount.put(categoryVO.getCode(), sum);
        }
        return totalCount;
    }

    @Data
    private static class ExportDistributionVO {
        private String distributionCategoryName;
        private String distributionCategoryPrice;
        private String distributionCount;
        private String distributionPrice;
    }
}
