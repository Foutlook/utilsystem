package com.foutin.excel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Locale;
import java.util.Map;

/**
 * @Description 有数据时日期时间展示样式
 */
public class HaveDatePickerWriteHandler implements CellWriteHandler {


    private final Map<Integer, String> columnIndexMap;

    public HaveDatePickerWriteHandler(Map<Integer, String> columnIndexMap) {
        this.columnIndexMap = columnIndexMap;
    }


    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell,
                                Head head, Integer relativeRowIndex, Boolean isHead) {
        if (Boolean.FALSE.equals(isHead)) {
            for (Map.Entry<Integer, String> entry : columnIndexMap.entrySet()) {
                Integer key = entry.getKey();
                if (cell.getColumnIndex() == key) {
                    Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
                    DataFormat dataFormat = workbook.createDataFormat();
                    CellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.setDataFormat(dataFormat.getFormat(entry.getValue().toLowerCase(Locale.getDefault())));
                    cell.setCellStyle(cellStyle);
                    break;
                }
            }
        }
    }


}
