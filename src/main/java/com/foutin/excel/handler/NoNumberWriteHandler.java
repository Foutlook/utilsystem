package com.foutin.excel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * @Description: 无数据时数字类型单元格展示样式
 */
public class NoNumberWriteHandler implements SheetWriteHandler {

    /**
     * 列下标集合
     */
    private final Map<Integer, Integer> columnIndexMap;

    public NoNumberWriteHandler(Map<Integer, Integer> columnIndexMap) {
        this.columnIndexMap = columnIndexMap;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        DataFormat dataFormat = workbook.createDataFormat();
        Sheet sheet = workbook.getSheetAt(0);
        // 设置单元格格式
        for (Map.Entry<Integer, Integer> indexPrecisionEntry : columnIndexMap.entrySet()) {
            CellStyle cellStyle = workbook.createCellStyle();
            Integer value = indexPrecisionEntry.getValue();
            StringBuilder precision = new StringBuilder();
            if (value != null && !value.equals(0)) {
                precision.append("0.");
                for (int i = 0; i < value; i++) {
                    precision.append("0");
                }
                cellStyle.setDataFormat(dataFormat.getFormat(precision.toString()));
            } else {
                cellStyle.setDataFormat(dataFormat.getFormat("0"));
            }
            sheet.setDefaultColumnStyle(indexPrecisionEntry.getKey(), cellStyle);
        }
    }
}