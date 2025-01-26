package com.foutin.excel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * @Description: 有数据时 下拉单元格 样式
 */
public class HaveDataSelectWriteHandler implements CellWriteHandler {

    /**
     * 下拉框map
     * key-列序号
     * value-下拉框的内容
     */
    private final Map<Integer, String[]> dropDownMap;

    private CellStyle cellStyle;

    public HaveDataSelectWriteHandler(Map<Integer, String[]> dropDownMap) {
        this.dropDownMap = dropDownMap;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row,
                                 Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
        Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
        DataFormat dataFormat = workbook.createDataFormat();
        if (this.cellStyle == null) {
            CellStyle style = workbook.createCellStyle();
            style.setDataFormat(dataFormat.getFormat("@"));
            this.cellStyle = style;
        }
    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell,
                                Head head, Integer relativeRowIndex, Boolean isHead) {
        if (Boolean.FALSE.equals(isHead)) {
            for (Map.Entry<Integer, String[]> entry : dropDownMap.entrySet()) {
                Integer key = entry.getKey();
                if (cell.getColumnIndex() == key) {
                    cell.setCellStyle(cellStyle);
                    break;
                }
            }
        }
    }

}
