package com.codertq.selleradmin.utils.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
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
public class MergeCellWriteHandler extends AbstractMergeStrategy {
    CellStyle cellStyle;

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        if (relativeRowIndex == null || relativeRowIndex == 0) {
            return;
        }
        int rowIndex = cell.getRowIndex();
        int colIndex = cell.getColumnIndex();
        Sheet thisSheet = cell.getSheet();
        Row preRow = thisSheet.getRow(rowIndex - 1);
        Row thisRow = thisSheet.getRow(rowIndex);
        Cell preCell = preRow.getCell(colIndex);// 获取上一行的该格
        Cell tmpCell;
        List<CellRangeAddress> list = thisSheet.getMergedRegions();
        for (int i = 0; i < list.size(); i++) {
            CellRangeAddress cellRangeAddress = list.get(i);
            if (cellRangeAddress.containsRow(preCell.getRowIndex()) && cellRangeAddress.containsColumn(preCell.getColumnIndex())) {
                int lastColIndex = cellRangeAddress.getLastColumn();
                int firstColIndex = cellRangeAddress.getFirstColumn();
                CellRangeAddress cra = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), firstColIndex, lastColIndex);
                thisSheet.addMergedRegion(cra);
                for (int j = firstColIndex; j <= lastColIndex; j++) {
                    tmpCell = thisRow.getCell(j);
                    if (tmpCell == null) {
                        tmpCell = thisRow.createCell(j);
                    }
                    tmpCell.setCellStyle(preRow.getCell(j).getCellStyle());
                }
                return;
            }
        }
    }
}