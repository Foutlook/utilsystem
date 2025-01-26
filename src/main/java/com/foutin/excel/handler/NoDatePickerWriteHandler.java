package com.foutin.excel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Locale;
import java.util.Map;

/**
 * @Description 无数据时日期时间单元格展示样式
 */
public class NoDatePickerWriteHandler implements SheetWriteHandler {

    private final Map<Integer, String> columnIndexMap;

    public NoDatePickerWriteHandler(Map<Integer, String> columnIndexMap) {
        this.columnIndexMap = columnIndexMap;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet sheet = workbook.getSheetAt(0);
        DataFormat dataFormat = workbook.createDataFormat();
        // 设置单元格格式
        for (Map.Entry<Integer, String> entry : columnIndexMap.entrySet()) {
            CellStyle cellStyle = workbook.createCellStyle();
            short format = dataFormat.getFormat(entry.getValue().toLowerCase(Locale.getDefault()));
            cellStyle.setDataFormat(format);
            sheet.setDefaultColumnStyle(entry.getKey(), cellStyle);
        }
    }
}
