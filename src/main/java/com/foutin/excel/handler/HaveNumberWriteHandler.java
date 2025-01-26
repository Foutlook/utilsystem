package com.foutin.excel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * @Description: 有数据时数字类型展示样式
 */
public class HaveNumberWriteHandler implements CellWriteHandler {

    /**
     * 列下标集合
     */
    private final Map<Integer, Integer> columnIndexMap;

    public HaveNumberWriteHandler(Map<Integer, Integer> columnIndexMap) {
        this.columnIndexMap = columnIndexMap;
    }


    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell,
                                Head head, Integer relativeRowIndex, Boolean isHead) {
        if (Boolean.FALSE.equals(isHead)) {
            Integer value = columnIndexMap.get(cell.getColumnIndex());
            if (null == value) {
                return;
            }
            Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
            DataFormat dataFormat = workbook.createDataFormat();
            CellStyle cellStyle = workbook.createCellStyle();
            if (value.equals(0)) {
                cellStyle.setDataFormat(dataFormat.getFormat("0"));
            } else {
                String format = "0.%0" + value + "d";
                String precision = String.format(format, 0);
                cellStyle.setDataFormat(dataFormat.getFormat(precision));
            }
            cell.setCellStyle(cellStyle);
        }
    }

}