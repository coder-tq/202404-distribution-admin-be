package com.codertq.selleradmin.utils.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * author: coder_tq
 * date: 2024/4/30
 */
public class MergeCellWriteHandler implements CellWriteHandler {
    @Data
    @Builder
    public static class MergeCellConfig {
        // 从哪行开始合并
        private int startRowIndex;
        //从哪列开始合并
        private int mergeColumnIndex;
        //需要合并的列数
        private int mergeColumnSize;
    }
    private List<MergeCellConfig> mergeCellConfigs;

    public MergeCellWriteHandler() {
    }

    public MergeCellWriteHandler(List<MergeCellConfig> mergeCellConfig) {
        this.mergeCellConfigs = mergeCellConfig;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

    }


    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        int currentRowIndex = cell.getRowIndex();
        int currentColumnIndex = cell.getColumnIndex();
        for (MergeCellConfig mergeCellConfig : mergeCellConfigs) {
            if (currentRowIndex < mergeCellConfig.startRowIndex) {
                continue;
            }
            Sheet sheet = writeSheetHolder.getSheet();
            List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
            if (currentColumnIndex == mergeCellConfig.mergeColumnIndex) {
                for (int i = 0; i < mergeRegions.size(); i++) {
                    CellRangeAddress cellRangeAddr = mergeRegions.get(i);
                    //  若当前单元格已经被合并，则先移出原有的合并单元，再重新添加合并单元
                    if (cellRangeAddr.isInRange(currentRowIndex, currentColumnIndex)) {
                        sheet.removeMergedRegion(i);
                        break;
                    }
                }
                Row row = cell.getRow();
                CellStyle cellStyle = cell.getCellStyle();
                Cell cell1 = row.getCell(currentColumnIndex);
                cell1.setCellStyle(cellStyle);
                CellRangeAddress cellRangeAddress = new CellRangeAddress(currentRowIndex, currentRowIndex, currentColumnIndex, currentColumnIndex + mergeCellConfig.mergeColumnSize);
                writeSheetHolder.getSheet().addMergedRegion(cellRangeAddress);
                cell.setCellStyle(cellStyle);
            }
        }
    }
}