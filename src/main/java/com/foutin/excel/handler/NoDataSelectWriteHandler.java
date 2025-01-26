package com.foutin.excel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.util.Map;

/**
 * @Description: 无数据时 下拉单元格 样式
 */
public class NoDataSelectWriteHandler implements SheetWriteHandler {

    /**
     * 下拉框map
     * key-列序号
     * value-下拉框的内容
     */
    private final Map<Integer, String[]> dropDownMap;

    public NoDataSelectWriteHandler(Map<Integer, String[]> dropDownMap) {
        this.dropDownMap = dropDownMap;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        if (MapUtils.isEmpty(dropDownMap)) {
            return;
        }
        Sheet sheet = writeSheetHolder.getSheet();
        // 获取一个workbook
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("@"));
        // 开始设置下拉框
        DataValidationHelper helper = sheet.getDataValidationHelper();
        if (helper != null) {
            for (Map.Entry<Integer, String[]> entry : dropDownMap.entrySet()) {
                // 列号
                Integer key = entry.getKey();
                // 下拉选项值
                String[] value = entry.getValue();
                // 创建隐藏sheet，保存下拉选项值
                // 定义sheet的名称
                String sheetName = "hidden_sheet_" + sheet.getSheetName() + key;
                // 1.创建一个隐藏的sheet
                Sheet hiddenSheet = workbook.createSheet(sheetName);
                hiddenSheet.setDefaultColumnStyle(0, cellStyle);
                // 设置隐藏
                int sheetIndex = workbook.getSheetIndex(hiddenSheet);
                workbook.setSheetHidden(sheetIndex, true);
                // 2.循环赋值（为了防止下拉框的行数与隐藏域的行数相对应，将隐藏域加到结束行之后）
                for (int i = 0, length = value.length; i < length; i++) {
                    // i:表示你开始的行数  0表示你开始的列数
                    hiddenSheet.createRow(i).createCell(0).setCellValue(value[i]);
                }
                Name category1Name = workbook.createName();
                if (category1Name != null) {
                    category1Name.setNameName(sheetName);
                    // 4 $A$1:$A$N代表 以A列1行开始获取N行下拉数据
                    if (value.length != 0) {
                        category1Name.setRefersToFormula(sheetName + "!$A$1:$A$" + (value.length));
                    }
                }

                int EXCEL_MAX_ROWS = 1048576;
                // 起始行、终止行、起始列、终止列
                CellRangeAddressList addressList = new CellRangeAddressList(2, EXCEL_MAX_ROWS - 1, key,
                        key);
                // 设置下拉框数据
                DataValidationConstraint constraint = helper.createFormulaListConstraint(sheetName);
                DataValidation dataValidation = helper.createValidation(constraint, addressList);
                dataValidation.createErrorBox("错误提示", "您输入的内容，不符合限制条件。请选择下拉框之内的值。");
                // 处理Excel兼容性问题
                if (dataValidation instanceof XSSFDataValidation) {
                    dataValidation.setSuppressDropDownArrow(true);
                    dataValidation.setShowErrorBox(true);
                } else {
                    dataValidation.setSuppressDropDownArrow(false);
                }
                sheet.setDefaultColumnStyle(key, cellStyle);
                sheet.addValidationData(dataValidation);
            }
        }
    }
}
