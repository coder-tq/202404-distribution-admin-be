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
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    public void exportDataToExcel(ZonedDateTime dateTime, HttpServletResponse response) {
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

    @Override
    public void exportAllDistributionDataToExcel(ZonedDateTime dateTime, HttpServletResponse response) {
        // Prepare the response
        response.setContentType("application/zip");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
        response.setHeader("Content-Disposition", "attachment; filename="+ DateTimeUtil.getLocalDateTime(dateTime).format(dateTimeFormatter) +".zip");

        DecimalFormat format = new DecimalFormat("0.######");
        String tempDir = System.getProperty("java.io.tmpdir");
        List<File> files = new ArrayList<>();


        ClassPathResource classPathResource = new ClassPathResource("templates/配货的模板.xlsx");
        List<DistributionVO> currentDistributionList = distributionMPService.getCurrentDistributionList(dateTime);
        Map<String, Integer> distributionNameCount = new HashMap<>();
        for (DistributionVO distributionVO : currentDistributionList) {
            Integer count = distributionNameCount.get(distributionVO.getDistributorName());
            String suffix = count == null ? "" : "_" + count;
            distributionNameCount.put(distributionVO.getDistributorName(), count == null ? 1 : count + 1);
            File tempFile = new File(tempDir, distributionVO.getDistributorName() + suffix + ".xlsx");
            files.add(tempFile);
            try (OutputStream out = new FileOutputStream(tempFile)) {
                ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(classPathResource.getInputStream()).registerWriteHandler(new MergeCellWriteHandler()).excelType(ExcelTypeEnum.XLSX).build();
                List<ExportDistributionVO> list = new ArrayList<>();
                Double totalCount = 0.0;
                for (DistributionDetailVO distributionDetailVO : distributionVO.getDistributionDetailList()) {
                    if (Double.parseDouble(distributionDetailVO.getCount()) == 0) {
                        continue;
                    }
                    ExportDistributionVO exportDistributionVO = new ExportDistributionVO();
                    exportDistributionVO.setDistributionCategoryName(distributionDetailVO.getCategoryName());
                    exportDistributionVO.setDistributionCategoryPrice(format.format(Double.parseDouble(distributionDetailVO.getPrice())));
                    exportDistributionVO.setDistributionCount(format.format(Double.parseDouble(distributionDetailVO.getCount())));
                    Double price = Double.parseDouble(distributionDetailVO.getCount()) * Double.parseDouble(distributionDetailVO.getPrice());
                    totalCount += Double.parseDouble(distributionDetailVO.getCount());
                    exportDistributionVO.setDistributionPrice(format.format(price));
                    list.add(exportDistributionVO);
                }
                WriteSheet writeSheet = EasyExcel.writerSheet().build();
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
                excelWriter.fill(list, fillConfig, writeSheet);
                Map<String, Object> map = MapUtils.newHashMap();
                map.put("distributorName", distributionVO.getDistributorName());
                LocalDate localDate = DateTimeUtil.getLocalDate(dateTime);
                map.put("year", localDate.getYear());
                map.put("month", localDate.getMonth().getValue());
                map.put("day", localDate.getDayOfMonth());
                map.put("totalCount", totalCount);
                excelWriter.fill(map, writeSheet);
                excelWriter.finish();
            } catch (IOException e) {
                // Handle the exception
            }
        }
        generateZipFile(response, files);
    }

    private void generateZipFile(HttpServletResponse response, List<File> files) {
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
    public void exportAllDistributionDataWithPriceToExcel(ZonedDateTime dateTime, HttpServletResponse response) {
        // Prepare the response
        response.setContentType("application/zip");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
        response.setHeader("Content-Disposition", "attachment; filename="+ DateTimeUtil.getLocalDateTime(dateTime).format(dateTimeFormatter) +".zip");

        DecimalFormat format = new DecimalFormat("0.######");
        String tempDir = System.getProperty("java.io.tmpdir");
        List<File> files = new ArrayList<>();


        ClassPathResource classPathResource = new ClassPathResource("templates/带价格的模板.xlsx");
        List<DistributionVO> currentDistributionList = distributionMPService.getCurrentDistributionList(dateTime);
        Map<String, Integer> distributionNameCount = new HashMap<>();
        for (DistributionVO distributionVO : currentDistributionList) {
            Integer count = distributionNameCount.get(distributionVO.getDistributorName());
            String suffix = count == null ? "" : "_" + count;
            distributionNameCount.put(distributionVO.getDistributorName(), count == null ? 1 : count + 1);
            File tempFile = new File(tempDir, distributionVO.getDistributorName() + suffix + ".xlsx");
            files.add(tempFile);
            try (OutputStream out = new FileOutputStream(tempFile)) {
                ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(classPathResource.getInputStream()).registerWriteHandler(new MergeCellWriteHandler()).excelType(ExcelTypeEnum.XLSX).build();
                List<ExportDistributionVO> list = new ArrayList<>();
                Double totalPrice = 0.0;
                for (DistributionDetailVO distributionDetailVO : distributionVO.getDistributionDetailList()) {
                    if (Double.parseDouble(distributionDetailVO.getCount()) == 0) {
                        continue;
                    }
                    ExportDistributionVO exportDistributionVO = new ExportDistributionVO();
                    exportDistributionVO.setDistributionCategoryName(distributionDetailVO.getCategoryName());
                    exportDistributionVO.setDistributionCategoryPrice(format.format(Double.parseDouble(distributionDetailVO.getPrice())));
                    exportDistributionVO.setDistributionCount(format.format(Double.parseDouble(distributionDetailVO.getCount())));
                    Double price = Double.parseDouble(distributionDetailVO.getCount()) * Double.parseDouble(distributionDetailVO.getPrice());
                    totalPrice += price;
                    exportDistributionVO.setDistributionPrice(format.format(price));
                    list.add(exportDistributionVO);
                }
                WriteSheet writeSheet = EasyExcel.writerSheet().build();
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
                excelWriter.fill(list, fillConfig, writeSheet);
                Map<String, Object> map = MapUtils.newHashMap();
                map.put("distributorName", distributionVO.getDistributorName());
                LocalDate localDate = DateTimeUtil.getLocalDate(dateTime);
                map.put("year", localDate.getYear());
                map.put("month", localDate.getMonth().getValue());
                map.put("day", localDate.getDayOfMonth());
                map.put("totalPrice", totalPrice);
                excelWriter.fill(map, writeSheet);
                excelWriter.finish();
            } catch (IOException e) {
                // Handle the exception
            }
        }
        generateZipFile(response, files);
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
