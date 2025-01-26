/*
 * ------------------------------------------------------------------
 * Copyright @ 2021 Hangzhou DtDream Technology Co.,Ltd. All rights reserved.
 * ------------------------------------------------------------------
 *       Product: JZDAS
 *   Module Name:
 *  Date Created: 2021/12/22
 *   Description:
 * ------------------------------------------------------------------
 * Modification History
 * DATE            Name           Description
 * ------------------------------------------------------------------
 * 2021/12/22      l2102
 * ------------------------------------------------------------------
 */
package com.foutin.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.foutin.excel.handler.CustomCellStyleStrategy;
import com.foutin.excel.handler.HaveDataSelectWriteHandler;
import com.foutin.excel.handler.HaveDatePickerWriteHandler;
import com.foutin.excel.handler.HaveNumberWriteHandler;
import com.foutin.excel.handler.NoDataSelectWriteHandler;
import com.foutin.excel.handler.NoDatePickerWriteHandler;
import com.foutin.excel.handler.NoNumberWriteHandler;
import com.foutin.excel.listener.FormExcelDataListener;
import com.foutin.excel.model.ComplexHeadStyles;
import com.foutin.excel.model.ControlInitValueModel;
import com.foutin.excel.model.DcFormOptionValueVO;
import com.foutin.excel.model.SheetModel;
import com.foutin.excel.stategy.CustomColumnWidthStyleStrategy;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.lang.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
public final class ExcelUtils {

    private static Map<String, String> DATE_PICKER_TYPES = new HashMap<>();

    static {
        // 日期选择 YYYY-MM-DD
        DATE_PICKER_TYPES.put("0", "YYYY-MM-DD");
        // 时间选择 HH:mm:ss
        DATE_PICKER_TYPES.put("1", "HH:mm:ss");
        // 时间日期选择 YYYY-MM-DD HH:mm:ss
        DATE_PICKER_TYPES.put("2", "YYYY-MM-DD HH:mm:ss");
        // 月份选择 YYYY-MM
        DATE_PICKER_TYPES.put("3", "YYYY-MM");
        // 年份选择 YYYY
        DATE_PICKER_TYPES.put("6", "YYYY");
    }

    private ExcelUtils() {
        // do nothing
    }

    /**
     * 将数据写入excel并包装成ByteArray字节数组返回
     *
     * @param headers         excel头部
     * @param useDefaultStyle useDefaultStyle
     * @return byte[]
     */
    public static byte[] writeExcelWithTitle(List<String> headers, Boolean useDefaultStyle) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             XSSFWorkbook xssfWorkbook = new XSSFWorkbook()) {
            XSSFSheet sheet = xssfWorkbook.createSheet("Sheet1");
            XSSFCellStyle cellStyle = setDefaultCellStyle(xssfWorkbook.createCellStyle(), useDefaultStyle);
            XSSFFont font = setFontStyle(xssfWorkbook.createFont());
            cellStyle.setFont(font);
            XSSFRow row = sheet.createRow(0);
            XSSFRow rowFirst = sheet.createRow(1);
            row.setHeightInPoints(Boolean.TRUE.equals(useDefaultStyle) ? 120 : 15);
            rowFirst.setHeightInPoints(15);
            for (int j = 0; j < headers.size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellValue(headers.get(j));
                cell.setCellStyle(cellStyle);
                if (Boolean.FALSE.equals(useDefaultStyle)) {
                    sheet.autoSizeColumn((short) j);
                    sheet.setColumnWidth(j, sheet.getColumnWidth(j) * 17 / 10);
                }
                XSSFCell cellFirst = rowFirst.createCell(j);
                if (j % 2 == 0) {
                    cellFirst.setCellValue("选项" + (j / 2));
                } else {
                    cellFirst.setCellValue((j - 1) / 2);
                }
            }
            xssfWorkbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error("生成模板失败: {}", e.getMessage());
            throw new RuntimeException("生成模板失败");
        }
    }

    public static byte[] writeExcelWithTitle(List<String> headers, List<String> select) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             XSSFWorkbook xssfWorkbook = new XSSFWorkbook()) {
            XSSFSheet sheet = xssfWorkbook.createSheet("Sheet1");
            XSSFCellStyle cellStyle = setDefaultCellStyle(xssfWorkbook.createCellStyle(), false);
            XSSFFont font = setFontStyle(xssfWorkbook.createFont());
            cellStyle.setFont(font);
            XSSFRow row = sheet.createRow(0);
            row.setHeightInPoints(15);
            for (int j = 0; j < headers.size(); j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellValue(headers.get(j));
                cell.setCellStyle(cellStyle);
                sheet.autoSizeColumn((short) j);
                sheet.setColumnWidth(j, sheet.getColumnWidth(j) * 17 / 10);
            }
            setHSSFValidation(sheet, select.toArray(new String[0]), 1, 500, 0, 0);
            xssfWorkbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            log.error("生成模板失败: {}", e.getMessage());
            throw new RuntimeException("生成模板失败");
        }
    }

    /**
     * 导出表单excel数据模板
     *
     * @param formName          表单名称
     * @param metaInfo          表单元信息
     * @param originalExcelData 表单填报的数据。为空: 导出模板有示例样例；不为空：originalExcelData数据会导入到excel中
     * @return
     */
    public static byte[] generatedDataFormExcel(String formName, String metaInfo,
                                                @Nullable List<JSONObject> originalExcelData) {
        // 排除不需要导出的控件和隐藏控件
        List<JSONObject> controlJson = getSupportControl(metaInfo);
        if (CollectionUtils.isEmpty(controlJson)) {
            return new byte[0];
        }

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ExcelWriter excelWriter = EasyExcelFactory.write(bos).excelType(ExcelTypeEnum.XLSX).build();
            // 获取下拉单选、下拉多选、单项选择、多项选择、树形单选、树形多选、级联多选控件对应选项值列表
            Map<String, List<DcFormOptionValueVO>> controlOptionValueMap =
                    FormParseUtils.getControlOptionValue(controlJson, metaInfo);
            // 获取填报sheet0
            AtomicInteger sheetNo = new AtomicInteger(0);
            // 构建表单excel模板中的填报数据
            List<List<String>> exampleValue = buildExcelExternalData(originalExcelData, controlJson,
                    controlOptionValueMap);
            // 生成表单模板Sheet
            SheetModel sheetModel0 = getSheetModel(formName, controlJson, false, controlOptionValueMap);
            WriteSheet sheet0 = ExcelUtils.createWriteSheet(sheetNo, sheetModel0);
            excelWriter.write(exampleValue, sheet0);
            // 控件默认值model列表
            List<ControlInitValueModel> controlInitValueModels = new ArrayList<>();
            List<JSONObject> defaultValueControl = FormParseUtils.getDefaultValueControl(controlJson,
                    controlInitValueModels);
            if (CollectionUtils.isNotEmpty(defaultValueControl)) {
                // 获取默认值sheet
                sheetNo.incrementAndGet();
                SheetModel sheetModel1 = getSheetModel("默认值", defaultValueControl, true, controlOptionValueMap);
                WriteSheet sheet1 = ExcelUtils.createWriteSheet(sheetNo, sheetModel1);
                // 获取默认值label
                List<String> defaultValueLabel = getDefaultValueLabel(controlInitValueModels, controlOptionValueMap);
                excelWriter.write(Collections.singletonList(defaultValueLabel), sheet1);
            }

            // 构建控件编码表
            buildControlCodingTable(controlJson, controlOptionValueMap, excelWriter, sheetNo);

            excelWriter.finish();
            return bos.toByteArray();
        } catch (Exception e) {
            log.error("导出表单excel模板异常：表单名称：{}", formName, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析表单填报excel
     *
     * @param file     excel文件
     * @param formName 表单名称
     * @param metaInfo 表单元数据
     * @return 解析结果
     */
    public static List<JSONObject> parseDataFormExcel(File file, String formName, String metaInfo) {
        try {
            EasyExcelFactory.read(file, new FormExcelDataListener(metaInfo))
                    .headRowNumber(getHeadRowNum(metaInfo))
                    .sheet(0).doRead();
            return ExcelAnalysisResultContext.get();
        } catch (Exception e) {
            log.error("解析失败，表单：{}", formName, e);
            throw new RuntimeException("解析失败，表单：" + formName);
        } finally {
            ExcelAnalysisResultContext.remove();
        }
    }


    /**
     * 构建excel的填充数据
     *
     * @param originalExcelData     填报控件的值
     * @param allControlJson        所有支持控件
     * @param controlOptionValueMap 控件选项值map
     * @return excel填充数据
     */
    private static List<List<String>> buildExcelExternalData(List<JSONObject> originalExcelData,
                                                             List<JSONObject> allControlJson,
                                                             Map<String, List<DcFormOptionValueVO>> controlOptionValueMap) {
        // 基础信息模板列索引
        Map<String, Integer> innerKeyIndexMap = new HashMap<>();
        // 信息项模板列索引
        Map<String, Integer> infoItemsIndexMap = new HashMap<>();
        // 记录innerKey与ReactKey的映射关系
        Map<String, String> innerReactKeyMap = new HashMap<>();
        // innerKey与控件的映射关系
        Map<String, JSONObject> innerTypeMap = new HashMap<>();

        // 构建控件innerKey、reactKey和列索引关系
        buildControlIndexMap(allControlJson, innerKeyIndexMap, infoItemsIndexMap, innerReactKeyMap, innerTypeMap);

        List<List<String>> externalData = new ArrayList<>();
        if (CollectionUtils.isEmpty(originalExcelData)) {
            // 构建excel样例数据
            buildExcelSampleData(allControlJson, controlOptionValueMap, externalData);
        } else {
            // 构建excel填报数据
            buildExcelFillData(originalExcelData, controlOptionValueMap, externalData, innerKeyIndexMap,
                    infoItemsIndexMap, innerReactKeyMap, innerTypeMap);
        }
        return externalData;
    }

    /**
     * 构建excel填报数据
     *
     * @param originalExcelData     填报数据
     * @param controlOptionValueMap 控件选项值
     * @param externalData          excel导出数据
     * @param innerKeyIndexMap      基础信息模板列索引
     * @param infoItemsIndexMap     信息项模板列索引
     * @param innerReactKeyMap      内部key与reactKey的映射
     * @param innerTypeMap          内部key与控件的映射
     */
    private static void buildExcelFillData(List<JSONObject> originalExcelData,
                                           Map<String, List<DcFormOptionValueVO>> controlOptionValueMap,
                                           List<List<String>> externalData,
                                           Map<String, Integer> innerKeyIndexMap,
                                           Map<String, Integer> infoItemsIndexMap,
                                           Map<String, String> innerReactKeyMap,
                                           Map<String, JSONObject> innerTypeMap) {
        int i = 0;
        int totalColumnNum = innerKeyIndexMap.size() + infoItemsIndexMap.size() + 1;
        for (JSONObject originalExcelDatum : originalExcelData) {
            // 初始化每行列数据
            List<String> rowData = new ArrayList<>(Collections.nCopies(totalColumnNum, null));
            // 填充序号
            rowData.set(0, String.valueOf(i++));
            externalData.add(rowData);
            // 遍历所有控件，获取控件类型，并根据类型组装行数据
            for (Map.Entry<String, JSONObject> entryType : innerTypeMap.entrySet()) {
                String innerKey = entryType.getKey();
                // 获取控件类型
                String type = entryType.getValue().getString(TcFormControlConstants.TYPE);
                if (TcFormControlTypeEnum.CUSTOMTABLE.getName().equals(type)) {
                    // 通过信息项模板列索引填充表格列
                    fillExcelCustomTableColumnData(controlOptionValueMap, externalData, infoItemsIndexMap,
                            innerReactKeyMap, totalColumnNum, originalExcelDatum, rowData, entryType.getKey(),
                            entryType.getValue());
                } else {
                    // 获取innerKey对应的reactKey
                    String reactKey = innerReactKeyMap.get(innerKey);
                    // 通过基础信息模板列索引填充非表格列
                    fillExcelColumnData(controlOptionValueMap, innerKeyIndexMap.get(innerKey), reactKey,
                            rowData, entryType.getValue(), innerKey, originalExcelDatum);
                }
            }
        }
    }

    private static void buildExcelSampleData(List<JSONObject> allControlJson,
                                             Map<String, List<DcFormOptionValueVO>> controlOptionValueMap,
                                             List<List<String>> externalData) {
        Boolean secondFlag = initExample(externalData, allControlJson);
        for (JSONObject control : allControlJson) {
            String reactKey = control.getString(TcFormControlConstants.REACT_KEY);
            List<DcFormOptionValueVO> optionValueList = controlOptionValueMap.get(reactKey);
            String type = control.getString(TcFormControlConstants.TYPE);
            TcFormControlTypeEnum controlTypeEnum = TcFormControlTypeEnum.match(type);
            switch (controlTypeEnum) {
                case INPUT:
                case TEXTAREA:
                case TAGS:
                    setTextValue(externalData, secondFlag);
                    break;
                case SELECT:
                case RADIO:
                case TREESELECT:
                case CASCADE:
                    setSelectValue(externalData, optionValueList, secondFlag, Boolean.FALSE);
                    break;
                case MULTIPLESELECT:
                case CHECKBOX:
                case TREEMULTIPLESELECT:
                    setSelectValue(externalData, optionValueList, secondFlag, Boolean.TRUE);
                    break;
                case INPUTNUMBER:
                    // 数字设置精度
                    setInputNumberValue(externalData, control.getInteger(TcFormControlConstants.PRECISION),
                            secondFlag);
                    break;
                case DATEPICKER:
                    // 设置日期格式
                    JSONObject formatJson =
                            control.getJSONObject(TcFormControlConstants.CHECK_OUT).getJSONObject(TcFormControlConstants.FORMAT);
                    setDatePickerValue(externalData, formatJson.getString(TcFormControlConstants.FORMAT),
                            secondFlag);
                    break;
                case CUSTOMTABLE:
                    setCustomTableValue(control, externalData, controlOptionValueMap);
                    break;
                default:
            }
        }
    }

    /**
     * 填充excel表格数据
     */
    private static void fillExcelCustomTableColumnData(Map<String, List<DcFormOptionValueVO>> controlOptionValueMap,
                                                       List<List<String>> externalData,
                                                       Map<String, Integer> infoItemsIndexMap,
                                                       Map<String, String> innerReactKeyMap, int totalColumnNum,
                                                       JSONObject originalExcelDatum, List<String> rowData,
                                                       String customInnerKey,
                                                       JSONObject customControlJson) {
        JSONArray infoItems = originalExcelDatum.getJSONArray(customInnerKey);
        if (CollectionUtils.isEmpty(infoItems)) {
            return;
        }
        // 获取表格子控件
        JSONArray childControlArray = customControlJson.getJSONArray(TcFormControlConstants.COLUMNS);
        List<JSONObject> childControlList = childControlArray.toJavaList(JSONObject.class);
        Map<String, JSONObject> childInnerKeyTypeMap =
                childControlList.stream().collect(Collectors.toMap(item -> item.getString(TcFormControlConstants.DATA_INDEX),
                        Function.identity()));

        List<JSONObject> childInfoItemsList = infoItems.toJavaList(JSONObject.class);
        for (int itemIndex = 0; itemIndex < childInfoItemsList.size(); itemIndex++) {
            JSONObject childJsonObject = childInfoItemsList.get(itemIndex);
            // 信息项第一行和基础信息同一行, 其他行另起一行，基础信息留空
            List<String> infoItemRowData;
            if (itemIndex == 0) {
                infoItemRowData = rowData;
            } else {
                infoItemRowData = new ArrayList<>(Collections.nCopies(totalColumnNum, null));
                externalData.add(infoItemRowData);
            }
            for (Map.Entry<String, Integer> entry : infoItemsIndexMap.entrySet()) {
                // 获取innerKey对应的reactKey
                String reactKey = innerReactKeyMap.get(entry.getKey());
                // 获取子控件
                JSONObject childControlJson = childInnerKeyTypeMap.get(entry.getKey());
                // 通过信息项列模板索引填充表格列
                fillExcelColumnData(controlOptionValueMap, entry.getValue(), reactKey, infoItemRowData,
                        childControlJson, entry.getKey(), childJsonObject);
            }
        }
    }


    /**
     * 填充excel列值
     *
     * @param controlOptionValueMap 选项值map
     * @param infoIndex             列索引
     * @param reactKey              控件reactKey
     * @param infoRowData           excel列对应的值集合
     * @param controlJson           控件元数据
     * @param innerKey              控件内部关键字
     * @param originalExcelDatum    填报数据
     */
    private static void fillExcelColumnData(Map<String, List<DcFormOptionValueVO>> controlOptionValueMap,
                                            Integer infoIndex,
                                            String reactKey,
                                            List<String> infoRowData,
                                            JSONObject controlJson,
                                            String innerKey,
                                            JSONObject originalExcelDatum) {
        String controlType = controlJson.getString(TcFormControlConstants.TYPE);
        // 获取innerKey对应的填报值
        String itemValue = Optional.ofNullable(originalExcelDatum.getString(innerKey)).orElse("");
        // 获取reactKey对应的选项值
        List<DcFormOptionValueVO> optionValue = controlOptionValueMap.get(reactKey);
        if (CollectionUtils.isNotEmpty(optionValue)) {
            // label 转value
            String itemLabel = null;
            if (StringUtils.equalsAny(controlType, TcFormControlTypeEnum.SELECT.getName(),
                    TcFormControlTypeEnum.RADIO.getName(), TcFormControlTypeEnum.MULTIPLESELECT.getName(),
                    TcFormControlTypeEnum.CHECKBOX.getName())) {
                // 获取选择控件选项值value对应的label
                itemLabel = getSelectRadioOptionLabel(innerKey, originalExcelDatum, controlType, itemValue,
                        optionValue);
            } else if (StringUtils.equalsAny(controlType, TcFormControlTypeEnum.TREESELECT.getName(),
                    TcFormControlTypeEnum.TREEMULTIPLESELECT.getName())) {
                // 获取树控件选项值value对应的label
                itemLabel = getTreeOptionLabel(innerKey, originalExcelDatum, controlType, itemValue, optionValue);
            } else if (StringUtils.equalsAny(controlType, TcFormControlTypeEnum.CASCADE.getName())) {
                // 获取级联控件选项值value对应的label
                itemLabel = getCascadeOptionLabel(controlJson, innerKey, originalExcelDatum, optionValue);
            } else {
                // 其他控件
                itemLabel = itemValue;
            }
            infoRowData.set(infoIndex, itemLabel);
        } else {
            // 无选择值控件label=value
            infoRowData.set(infoIndex, itemValue);
        }
    }

    private static String getCascadeOptionLabel(JSONObject controlJson, String innerKey, JSONObject originalExcelDatum,
                                                List<DcFormOptionValueVO> optionValue) {
        String itemLabel = null;
        Map<String, String> optionMap = FormParseUtils.getTreeOrCascadeOption(optionValue, "",
                "", false);
        // 获取级联的所有innerKey
        List<String> innerKeys = controlJson.getJSONArray(TcFormControlConstants.ATTRS).stream()
                .map(jsonObject -> ((JSONObject) jsonObject).getString(TcFormControlConstants.INNERKEY))
                .collect(Collectors.toList());
        JSONObject valueJsonObject = originalExcelDatum.getJSONObject(innerKey);
        if (null != valueJsonObject) {
            String innerKeysStr =
                    innerKeys.stream().map(valueJsonObject::getString).collect(Collectors.joining("/"));
            itemLabel = optionMap.get(innerKeysStr);
        }
        return itemLabel;
    }

    private static String getTreeOptionLabel(String innerKey, JSONObject originalExcelDatum, String controlType,
                                             String itemValue, List<DcFormOptionValueVO> optionValue) {
        String itemLabel = null;
        Map<String, String> optionMap = FormParseUtils.getTreeOrCascadeOption(optionValue, "",
                "", true);
        if (StringUtils.equalsAny(controlType, TcFormControlTypeEnum.TREESELECT.getName())) {
            itemLabel = getMultipleValueLabel(optionMap, Collections.singletonList(itemValue));
        } else {
            // 获取innerKey对应的填报值
            JSONArray valueJsonArray = originalExcelDatum.getJSONArray(innerKey);
            if (null != valueJsonArray) {
                List<String> itemValueList = valueJsonArray.toJavaList(String.class);
                itemLabel = getMultipleValueLabel(optionMap, itemValueList);
            }
        }
        return itemLabel;
    }

    private static String getSelectRadioOptionLabel(String innerKey, JSONObject originalExcelDatum, String controlType,
                                                    String itemValue, List<DcFormOptionValueVO> optionValue) {
        String itemLabel = null;
        // 转换控件选项值为map结构
        Map<String, String> optionMap = FormParseUtils.getSelectOption(optionValue);
        if (StringUtils.equalsAny(controlType, TcFormControlTypeEnum.SELECT.getName(),
                TcFormControlTypeEnum.RADIO.getName())) {
            itemLabel = optionMap.get(itemValue);
        } else {
            // 获取innerKey对应的填报值
            JSONArray valueJsonArray = originalExcelDatum.getJSONArray(innerKey);
            if (null != valueJsonArray) {
                List<String> itemValueList = valueJsonArray.toJavaList(String.class);
                itemLabel = itemValueList.stream().map(optionMap::get).collect(Collectors.joining(","));
            }
        }
        return itemLabel;
    }

    private static String getMultipleValueLabel(Map<String, String> optionMap, List<String> itemValueList) {
        String itemLabel;
        itemLabel = itemValueList.stream().map(itemValue -> {
            Optional<String> first = optionMap.entrySet().stream().filter(optionEntry -> {
                String[] vs = optionEntry.getKey().split("/");
                return vs[vs.length - 1].equals(itemValue);
            }).map(Map.Entry::getValue).findFirst();
            return first.orElse("");
        }).collect(Collectors.joining(","));
        return itemLabel;
    }

    /**
     * 构建控件innerKey、reactKey、控件类型和列索引关系
     *
     * @param allControlJson    所有控件，注意：这里已经排除了excel不支持的控件，请看：getSupportControl方法
     * @param innerKeyIndexMap  基础信息模板列索引 innerKey和列索引
     * @param infoItemsIndexMap 表格模板列索引 innerKey和列索引
     * @param innerReactKeyMap  innerKey和reactKey
     * @param innerTypeMap      innerKey和控件类型
     */
    private static void buildControlIndexMap(List<JSONObject> allControlJson,
                                             Map<String, Integer> innerKeyIndexMap,
                                             Map<String, Integer> infoItemsIndexMap,
                                             Map<String, String> innerReactKeyMap,
                                             Map<String, JSONObject> innerTypeMap) {
        int index = 1;
        for (JSONObject controlJson : allControlJson) {
            String type = controlJson.getString(TcFormControlConstants.TYPE);
            String controlInnerKey = controlJson.getString(TcFormControlConstants.INNER_KEY);
            innerTypeMap.put(controlInnerKey, controlJson);
            if (TcFormControlTypeEnum.CUSTOMTABLE.getName().equals(type)) {
                JSONArray childControlArray = controlJson.getJSONArray(TcFormControlConstants.COLUMNS);
                List<JSONObject> childControlList = childControlArray.toJavaList(JSONObject.class);
                for (JSONObject childControl : childControlList) {
                    String childInnerKey = childControl.getString(TcFormControlConstants.DATA_INDEX);
                    infoItemsIndexMap.put(childInnerKey, index++);
                    innerReactKeyMap.put(childInnerKey, childControl.getString(TcFormControlConstants.KEY));
                }
            } else {
                innerKeyIndexMap.put(controlInnerKey, index++);
                innerReactKeyMap.put(controlInnerKey, controlJson.getString(TcFormControlConstants.REACT_KEY));
            }
        }
    }

    private static int getHeadRowNum(String metaInfo) {
        // List<JSONObject> allControlJson = TcCommonUtils.getAllControlJson(metaInfo);
        List<JSONObject> allControlJson = null;
        Optional<JSONObject> first =
                allControlJson.stream().filter(control ->
                                control.getString(TcFormControlConstants.TYPE).equals(TcFormControlTypeEnum.CUSTOMTABLE.getName()))
                        .findFirst();
        return first.isPresent() ? 3 : 2;
    }

    public static List<JSONObject> getSupportControl(String metaInfo) {
        // List<JSONObject> allControlJson = TcCommonUtils.getAllControlJson(metaInfo);
        List<JSONObject> allControlJson = null;
        if (CollectionUtils.isEmpty(allControlJson)) {
            return Collections.emptyList();
        }
        return allControlJson.stream()
                .filter(control -> !StringUtils.endsWithAny(control.getString(TcFormControlConstants.TYPE),
                        TcFormControlTypeEnum.GRID.getName(),
                        TcFormControlTypeEnum.GROUP.getName(),
                        TcFormControlTypeEnum.UPLOAD.getName(),
                        TcFormControlTypeEnum.MAP.getName(),
                        TcFormControlTypeEnum.COORDINATE.getName(),
                        TcFormControlTypeEnum.MAPAIR.getName(),
                        TcFormControlTypeEnum.SELECTREC.getName(),
                        TcFormControlTypeEnum.CASCADEREC.getName(),
                        TcFormControlTypeEnum.DESCRIPTION.getName(),
                        TcFormControlTypeEnum.DATETERM.getName(),
                        TcFormControlTypeEnum.LINK.getName()))
                .filter(control -> Boolean.FALSE.equals(control.getBoolean(TcFormControlConstants.HIDE)))
                .filter(control -> {
                    // 过滤不支持导出的日期类型
                    if (TcFormControlTypeEnum.DATEPICKER.getName().equals(control.getString(TcFormControlConstants.TYPE))) {
                        JSONObject formatJson =
                                control.getJSONObject(TcFormControlConstants.CHECK_OUT).getJSONObject(TcFormControlConstants.FORMAT);
                        String value = formatJson.getString(TcFormControlConstants.VALUE);
                        return DATE_PICKER_TYPES.containsKey(value);
                    }
                    return true;
                }).collect(Collectors.toList());
    }


    /**
     * 获取默认值label
     *
     * @param controlInitValueModels 控件默认值列表
     * @param controlOptionValueMap  控件选项值map
     * @return 所有控件对应默认值
     */
    private static List<String> getDefaultValueLabel(List<ControlInitValueModel> controlInitValueModels,
                                                     Map<String, List<DcFormOptionValueVO>> controlOptionValueMap) {
        List<String> valueLabel = new ArrayList<>();
        controlInitValueModels.forEach(initValueModel -> {
            String type = initValueModel.getType();
            List<String> initValueList = initValueModel.getInitValue();
            List<DcFormOptionValueVO> optionValueList = controlOptionValueMap.get(initValueModel.getReactKey());
            if (CollectionUtils.isEmpty(optionValueList)) {
                valueLabel.add(String.join(",", initValueList));
                return;
            }
            TcFormControlTypeEnum controlTypeEnum = TcFormControlTypeEnum.match(type);
            switch (controlTypeEnum) {
                case CASCADE:
                    Map<String, String> cascadeOption = FormParseUtils.getTreeOrCascadeOption(optionValueList,
                            "", "", false);
                    String initValue = String.join("/", initValueList);
                    String initLabel = cascadeOption.get(initValue);
                    valueLabel.add(initLabel);
                    break;
                case TREESELECT:
                case TREEMULTIPLESELECT:
                    // 树形单选和多选，选择的时候不会关联父级，选项值单独个体存在
                    Map<String, String> treeOptionMap = FormParseUtils.getTreeOrCascadeOption(optionValueList,
                            "", "", true);
                    List<String> treeLabelList = initValueList.stream().map(value -> {
                        Optional<String> first = treeOptionMap.entrySet().stream().filter(entry -> {
                            String[] vs = entry.getKey().split(SymbolConstants.SINGLE_SLASH);
                            return vs[vs.length - 1].equals(value);
                        }).map(entry -> {
                            String[] split = entry.getValue().split(SymbolConstants.SINGLE_SLASH);
                            return split[split.length - 1];
                        }).findFirst();
                        return first.orElse("");
                    }).collect(Collectors.toList());
                    valueLabel.add(String.join(",", treeLabelList));
                    break;
                case MULTIPLESELECT:
                case CHECKBOX:
                case SELECT:
                case RADIO:
                    Map<String, String> selectOption = FormParseUtils.getSelectOption(optionValueList);
                    List<String> initLabelList =
                            initValueList.stream().map(selectOption::get).collect(Collectors.toList());
                    valueLabel.add(String.join(",", initLabelList));
                    break;
                default:
                    valueLabel.add(String.join(",", initValueList));
                    break;
            }
        });
        return valueLabel;
    }


    /**
     * 创建Sheet对象
     *
     * @param sheetNo    编号
     * @param sheetModel model对象
     * @return WriteSheet
     */
    public static WriteSheet createWriteSheet(AtomicInteger sheetNo, SheetModel sheetModel) {
        ExcelWriterSheetBuilder excelWriterSheetBuilder =
                EasyExcelFactory.writerSheet(sheetNo.get(), sheetModel.getSheetName()).head(sheetModel.getHeadList());
        if (CollectionUtils.isNotEmpty(sheetModel.getWriteHandlers())) {
            sheetModel.getWriteHandlers().forEach(excelWriterSheetBuilder::registerWriteHandler);
        }
        // 自定义自动列宽(不太精确)
        excelWriterSheetBuilder.registerWriteHandler(new CustomColumnWidthStyleStrategy(20));
        // 简单行高样式策略
        excelWriterSheetBuilder.registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 100, (short) 30));
        return excelWriterSheetBuilder.build();
    }


    /**
     * 获取sheet对象model
     *
     * @param sheetName             sheet名
     * @param controlJson           控件列表
     * @param isDefaultValue        是否默认值sheet
     * @param controlOptionValueMap 控件选择值列表
     * @return SheetModel
     */
    private static SheetModel getSheetModel(String sheetName, List<JSONObject> controlJson,
                                            boolean isDefaultValue,
                                            Map<String, List<DcFormOptionValueVO>> controlOptionValueMap) {
        // 列位置
        AtomicInteger columnIndex = new AtomicInteger(0);
        // 必填非表格列位置
        List<Integer> requiredColumnList = new ArrayList<>();
        // 必填表格子控件列位置
        List<Integer> requiredTableSubColumns = new ArrayList<>();
        // 数字类型Map<列序号，精度>
        Map<Integer, Integer> numberIndexMap = new HashMap<>();
        // 日期时间类型Map<列序号，格式>
        Map<Integer, String> datePickerIndexMap = new HashMap<>();
        // 表头
        List<List<String>> headList = buildHeadList(controlJson, isDefaultValue, columnIndex, requiredColumnList);
        // 选择处理，key-列序号，value-下拉框内容
        Map<Integer, String[]> selectedMap = new HashMap<>();

        for (JSONObject control : controlJson) {
            String reactKey = control.getString(TcFormControlConstants.REACT_KEY);
            List<DcFormOptionValueVO> optionValueList = controlOptionValueMap.get(reactKey);
            String type = control.getString(TcFormControlConstants.TYPE);
            TcFormControlTypeEnum controlTypeEnum = TcFormControlTypeEnum.match(type);
            switch (controlTypeEnum) {
                case INPUT:
                case TEXTAREA:
                case TAGS:
                case MULTIPLESELECT:
                case CHECKBOX:
                case TREEMULTIPLESELECT:
                    break;
                case SELECT:
                case RADIO:
                    setSelectMap(columnIndex, selectedMap, optionValueList);
                    break;
                case TREESELECT:
                    setTreeOrCascadeMap(columnIndex, selectedMap, true, optionValueList);
                    break;
                case CASCADE:
                    setTreeOrCascadeMap(columnIndex, selectedMap, false, optionValueList);
                    break;
                case INPUTNUMBER:
                    // 数字设置精度
                    setInputNumberMap(columnIndex, numberIndexMap, control);
                    break;
                case DATEPICKER:
                    // 设置日期格式
                    setDatePickerMap(columnIndex, datePickerIndexMap, control);
                    break;
                case CUSTOMTABLE:
                    setCustomTableMap(columnIndex, control, selectedMap, requiredColumnList,
                            requiredTableSubColumns, controlOptionValueMap);
                    break;
                default:
            }

            // 非表格必填列
            if (!type.equals(TcFormControlTypeEnum.CUSTOMTABLE.getName())) {
                Boolean required = control.getBoolean(TcFormControlConstants.REQUIRED);
                if (Boolean.TRUE.equals(required)) {
                    requiredColumnList.add(columnIndex.get());
                }
                columnIndex.incrementAndGet();
            }
        }

        List<WriteHandler> writeHandlers = Lists.newArrayList();
        // 自定义表头
        List<ComplexHeadStyles> complexHeadStyles = Lists.newArrayList();
        requiredColumnList.forEach(column -> {
            complexHeadStyles.add(new ComplexHeadStyles(0, column, IndexedColors.GOLD.getIndex()));
            complexHeadStyles.add(new ComplexHeadStyles(1, column,
                    IndexedColors.GOLD.getIndex()));
        });
        requiredTableSubColumns.forEach(column -> {
            complexHeadStyles.add(new ComplexHeadStyles(1, column,
                    IndexedColors.GOLD.getIndex()));
            complexHeadStyles.add(new ComplexHeadStyles(2, column,
                    IndexedColors.GOLD.getIndex()));
        });
        // 处理表格头样式
        writeHandlers.add(new CustomCellStyleStrategy(complexHeadStyles));

        // 自定义选择样式
        writeHandlers.add(new NoDataSelectWriteHandler(selectedMap));
        writeHandlers.add(new HaveDataSelectWriteHandler(selectedMap));

        // 数字类型处理
        writeHandlers.add(new NoNumberWriteHandler(numberIndexMap));
        writeHandlers.add(new HaveNumberWriteHandler(numberIndexMap));

        // 日期时间处理
        writeHandlers.add(new NoDatePickerWriteHandler(datePickerIndexMap));
        writeHandlers.add(new HaveDatePickerWriteHandler(datePickerIndexMap));


        SheetModel sheetModel = new SheetModel();
        sheetModel.setHeadList(headList);
        sheetModel.setSheetName(sheetName);
        sheetModel.setWriteHandlers(writeHandlers);
        return sheetModel;
    }


    private static void wrapExcelData(List<List<String>> excelData,
                                      Map<Integer, Map<String, String>> selectedValueMap) {
        for (List<String> rowData : excelData) {
            for (Integer colIndex : selectedValueMap.keySet()) {
                String optionKey = rowData.get(colIndex);
                if (StringUtils.isNotEmpty(optionKey)) {
                    Map<String, String> vauleMap = selectedValueMap.get(colIndex);
                    List<String> optionKeyList = Arrays.asList(optionKey.split(","));
                    List<String> treeLabelList = optionKeyList.stream().map((value) -> {
                        Optional<String> first = vauleMap.entrySet().stream().filter((entry) -> {
                            String[] vs = ((String) entry.getKey()).split("/");
                            return vs[vs.length - 1].equals(value);
                        }).map((entry) -> {
                            return entry.getValue();
                        }).findFirst();
                        return (String) first.orElse("");
                    }).collect(Collectors.toList());
                    rowData.set(colIndex, String.join(",", treeLabelList));
                }
            }
        }
    }

    /**
     * 初始化示例数据结构及数据
     *
     * @param exampleValue 示例值
     * @param controlJson  控件列表
     */
    private static Boolean initExample(List<List<String>> exampleValue, List<JSONObject> controlJson) {
        if (null == exampleValue) {
            return Boolean.FALSE;
        }
        List<String> firstLineExample = new ArrayList<>();
        List<String> secondLineExample = new ArrayList<>();
        firstLineExample.add("1");
        exampleValue.add(firstLineExample);
        exampleValue.add(secondLineExample);
        Boolean existCustomTableAndSecond = controlJson.stream()
                .anyMatch(control -> StringUtils.equals(
                        control.getString(TcFormControlConstants.TYPE), TcFormControlTypeEnum.CUSTOMTABLE.getName()));
        if (Boolean.TRUE.equals(existCustomTableAndSecond)) {
            exampleValue.get(1).add(null);
        }
        return existCustomTableAndSecond;
    }

    /**
     * 设置文本型控件示例
     *
     * @param exampleValue 示例值
     * @param secondFlag   是否存在第二行
     */
    private static void setTextValue(List<List<String>> exampleValue, Boolean secondFlag) {
        if (CollectionUtils.isEmpty(exampleValue)) {
            return;
        }
        exampleValue.get(0).add("示例");
        if (Boolean.TRUE.equals(secondFlag)) {
            exampleValue.get(1).add(null);
        }
    }

    /**
     * 设置选择类型控件示例
     *
     * @param exampleValue    示例值
     * @param optionValueList 选项集
     * @param secondFlag      是否存在第二行
     * @param multiSelect     是否多选型控件
     */
    private static void setSelectValue(
            List<List<String>> exampleValue,
            List<DcFormOptionValueVO> optionValueList,
            Boolean secondFlag,
            Boolean multiSelect) {
        if (CollectionUtils.isEmpty(exampleValue)) {
            return;
        }
        if (Boolean.TRUE.equals(multiSelect) && 1 < optionValueList.size()) {
            exampleValue.get(0).add(optionValueList.get(0).getLabel() + "," + optionValueList.get(1).getLabel());
        } else if (CollectionUtils.isNotEmpty(optionValueList)) {
            exampleValue.get(0).add(optionValueList.get(0).getLabel());
        } else {
            exampleValue.get(0).add("示例");
        }
        if (Boolean.TRUE.equals(secondFlag)) {
            exampleValue.get(1).add(null);
        }
    }

    /**
     * 设置数字类型控件示例
     *
     * @param exampleValue 示例值
     * @param precision    数值精度
     * @param secondFlag   是否存在第二行
     */
    private static void setInputNumberValue(
            List<List<String>> exampleValue,
            Integer precision,
            Boolean secondFlag) {
        if (CollectionUtils.isEmpty(exampleValue)) {
            return;
        }
        BigDecimal number = new BigDecimal(1);
        exampleValue.get(0).add(number.setScale(precision).toString());
        if (Boolean.TRUE.equals(secondFlag)) {
            exampleValue.get(1).add(null);
        }
    }

    /**
     * 设置日期时间控件示例
     *
     * @param exampleValue 示例值
     * @param format       日期时间格式
     * @param secondFlag   是否存在第二行
     */
    private static void setDatePickerValue(
            List<List<String>> exampleValue,
            String format,
            Boolean secondFlag) {
        if (CollectionUtils.isEmpty(exampleValue)) {
            return;
        }
        format = format.replace("YYYY", "yyyy").replace("DD", "dd").replace("wo", "ww");
        exampleValue.get(0).add(DateUtils.formatDateToString(new Date(), format));
        if (Boolean.TRUE.equals(secondFlag)) {
            exampleValue.get(1).add(null);
        }
    }

    /**
     * 设置表格控件示例
     *
     * @param control               当前控件
     * @param exampleValue          示例值
     * @param controlOptionValueMap 控件选项值map
     */
    private static void setCustomTableValue(JSONObject control,
                                            List<List<String>> exampleValue,
                                            Map<String, List<DcFormOptionValueVO>> controlOptionValueMap) {
        if (CollectionUtils.isEmpty(exampleValue)) {
            return;
        }
        JSONArray childControlArray = control.getJSONArray(TcFormControlConstants.COLUMNS);
        List<JSONObject> childControlList = childControlArray.toJavaList(JSONObject.class);
        for (JSONObject child : childControlList) {
            String type = child.getString(TcFormControlConstants.TYPE);
            String key = child.getString(TcFormControlConstants.KEY);
            List<DcFormOptionValueVO> optionValueList = controlOptionValueMap.get(key);
            if (StringUtils.equals(TcFormControlTypeEnum.SELECT.getName(), type)
                    || StringUtils.equals(TcFormControlTypeEnum.TREESELECT.getName(), type)) {
                String label = CollectionUtils.isNotEmpty(optionValueList) ? optionValueList.get(0).getLabel() : "示例";
                exampleValue.get(0).add(label);
                exampleValue.get(1).add(label);
            } else {
                exampleValue.get(0).add("示例");
                exampleValue.get(1).add("示例");
            }
        }
    }

    /**
     * 构建控件编码表
     *
     * @param controlJson           需要编码的控件
     * @param controlOptionValueMap 选项值map
     * @param excelWriter           excel对象
     * @param sheetNo               sheet编号
     */
    private static void buildControlCodingTable(List<JSONObject> controlJson,
                                                Map<String, List<DcFormOptionValueVO>> controlOptionValueMap,
                                                ExcelWriter excelWriter,
                                                AtomicInteger sheetNo) {
        // 生成多选、单选、下拉的编码表
        for (JSONObject control : controlJson) {
            String type = control.getString(TcFormControlConstants.TYPE);
            if (!StringUtils.equalsAny(type, TcFormControlTypeEnum.CHECKBOX.getName(),
                    TcFormControlTypeEnum.MULTIPLESELECT.getName(),
                    TcFormControlTypeEnum.TREEMULTIPLESELECT.getName(),
                    TcFormControlTypeEnum.CUSTOMTABLE.getName())) {
                continue;
            }

            // 处理表格控件
            if (TcFormControlTypeEnum.CUSTOMTABLE.getName().equals(type)) {
                JSONArray childControlArray = control.getJSONArray(TcFormControlConstants.COLUMNS);
                List<JSONObject> childControlList = childControlArray.toJavaList(JSONObject.class);
                // 递归
                buildControlCodingTable(childControlList, controlOptionValueMap, excelWriter, sheetNo);
                continue;
            }

            String label = control.getString(TcFormControlConstants.LABEL);
            if (StringUtils.isBlank(label)) {
                label = "表格-" + control.getString(TcFormControlConstants.TITLE);
            }
            String reactKey = control.getString(TcFormControlConstants.REACT_KEY);
            if (StringUtils.isBlank(reactKey)) {
                reactKey = control.getString(TcFormControlConstants.KEY);
            }
            List<DcFormOptionValueVO> dcFormOptionValue = controlOptionValueMap.get(reactKey);
            if (CollectionUtils.isEmpty(dcFormOptionValue)) {
                continue;
            }

            // 生成字典sheet
            sheetNo.incrementAndGet();
            List<List<String>> codeHead = new ArrayList<>(2);
            List<String> tempHead = new ArrayList<>(1);
            tempHead.add("选项名");
            codeHead.add(tempHead);
            tempHead = new ArrayList<>(1);
            tempHead.add("选项值");
            codeHead.add(tempHead);
            WriteSheet sheet =
                    EasyExcelFactory.writerSheet(sheetNo.get(), "【" + label + "】编码表").head(codeHead).build();

            List<List<String>> dataList = new ArrayList<>();
            Map<String, String> option;
            TcFormControlTypeEnum match = TcFormControlTypeEnum.match(type);
            switch (match) {
                case CASCADE:
                    option = FormParseUtils.getTreeOrCascadeOption(dcFormOptionValue, "", "", false);
                    break;
                case TREESELECT:
                case TREEMULTIPLESELECT:
                    option = FormParseUtils.getTreeOrCascadeOption(dcFormOptionValue, "", "", true);
                    break;
                default:
                    option = FormParseUtils.getSelectOption(dcFormOptionValue);
                    break;
            }
            option.forEach((key, value) -> {
                List<String> sheetData = new ArrayList<>();
                sheetData.add(value);
                sheetData.add(key);
                dataList.add(sheetData);
            });

            excelWriter.write(dataList, sheet);
        }
    }

    /**
     * 设置表格控件
     *
     * @param columnIndex                列编号
     * @param control                    当前控件
     * @param selectedMap                下拉选择map
     * @param requiredTableParentColumns 表格控件列下标
     * @param requiredTableSubColumns    表格子控件列下标
     * @param controlOptionValueMap      控件选项值map
     */
    private static void setCustomTableMap(AtomicInteger columnIndex, JSONObject control,
                                          Map<Integer, String[]> selectedMap,
                                          List<Integer> requiredTableParentColumns,
                                          List<Integer> requiredTableSubColumns,
                                          Map<String, List<DcFormOptionValueVO>> controlOptionValueMap) {
        JSONArray childControlArray = control.getJSONArray(TcFormControlConstants.COLUMNS);
        List<JSONObject> childControlList = childControlArray.toJavaList(JSONObject.class);
        // 表格控件必填
        Boolean parentRequired = control.getBoolean(TcFormControlConstants.REQUIRED);
        if (Boolean.TRUE.equals(parentRequired)) {
            requiredTableParentColumns.add(columnIndex.get());
        }

        for (JSONObject child : childControlList) {
            String type = child.getString(TcFormControlConstants.TYPE);
            String key = child.getString(TcFormControlConstants.KEY);
            List<DcFormOptionValueVO> optionValueList = controlOptionValueMap.get(key);
            if (TcFormControlTypeEnum.SELECT.getName().equals(type)) {
                setSelectMap(columnIndex, selectedMap, optionValueList);
            }
            if (TcFormControlTypeEnum.TREESELECT.getName().equals(type)) {
                setTreeOrCascadeMap(columnIndex, selectedMap, true, optionValueList);
            }

            // 子控件必填
            Boolean subRequired = child.getBoolean(TcFormControlConstants.REQUIRED);
            if (Boolean.TRUE.equals(subRequired)) {
                requiredTableSubColumns.add(columnIndex.get());
            }

            // 列索引增加1
            columnIndex.incrementAndGet();
        }
    }

    /**
     * 日期格式设置
     *
     * @param columnIndex        列编号
     * @param datePickerIndexMap 日期时间格式map
     * @param control            当前控件
     */
    private static void setDatePickerMap(AtomicInteger columnIndex, Map<Integer, String> datePickerIndexMap,
                                         JSONObject control) {
        JSONObject formatJson =
                control.getJSONObject(TcFormControlConstants.CHECK_OUT).getJSONObject(TcFormControlConstants.FORMAT);
        String format = formatJson.getString(TcFormControlConstants.FORMAT);
        datePickerIndexMap.put(columnIndex.get(), format);
    }

    /**
     * 设置数字类型精度
     *
     * @param columnIndex    列编号
     * @param columnIndexMap 数字类型精度map
     * @param control        当前控件
     */
    private static void setInputNumberMap(AtomicInteger columnIndex, Map<Integer, Integer> columnIndexMap,
                                          JSONObject control) {
        Integer precision = control.getInteger(TcFormControlConstants.PRECISION);
        if (precision != null && !precision.equals(0)) {
            // 小数
            columnIndexMap.put(columnIndex.get(), precision);
        } else {
            columnIndexMap.put(columnIndex.get(), 0);
        }
    }


    /**
     * 构建树形选择下拉框
     *
     * @param columnIndex 必填列
     * @param selectedMap 选择map
     * @param includeFlag 父节点禁选，子节点是否口选标识
     */
    private static void setTreeOrCascadeMap(AtomicInteger columnIndex,
                                            Map<Integer, String[]> selectedMap,
                                            boolean includeFlag,
                                            List<DcFormOptionValueVO> optionValueList) {
        if (CollectionUtils.isEmpty(optionValueList)) {
            return;
        }
        Map<String, String> treeOrCascadeOption = FormParseUtils.getTreeOrCascadeOption(optionValueList, "", "",
                includeFlag);
        Collection<String> labelList = treeOrCascadeOption.values();
        selectedMap.put(columnIndex.get(), labelList.toArray(new String[0]));
    }

    /**
     * 构建下拉选择下拉框
     *
     * @param columnIndex     必填列
     * @param selectedMap     选择map
     * @param optionValueList 选项值列表
     */
    private static void setSelectMap(AtomicInteger columnIndex, Map<Integer, String[]> selectedMap,
                                     List<DcFormOptionValueVO> optionValueList) {
        // AssertUtil.isNotEmpty(optionValueList, "【" + control.getString(TcFormControlConstants.LABEL) +
        // "】选项中必须包含一个可选");

        if (CollectionUtils.isEmpty(optionValueList)) {
            return;
        }

        // 获取选项label
        Map<String, String> selectOptionMap = FormParseUtils.getSelectOption(optionValueList);
        Collection<String> labelList = selectOptionMap.values();

        // 设置选项值到map中
        selectedMap.put(columnIndex.get(), labelList.toArray(new String[0]));
    }


    private static List<List<String>> buildHeadList(List<JSONObject> controlJson, boolean isDefaultValue,
                                                    AtomicInteger columnIndex,
                                                    List<Integer> requiredColumnList) {
        List<List<String>> list = new ArrayList<>();
        if (Boolean.FALSE.equals(isDefaultValue)) {
            List<String> number = new ArrayList<>();
            number.add(TcFormControlConstants.EXCEL_HEAD_STATEMENT);
            number.add("*序号");
            list.add(number);
            requiredColumnList.add(columnIndex.get());
            columnIndex.incrementAndGet();
        }
        for (JSONObject jsonObject : controlJson) {
            String type = jsonObject.getString(TcFormControlConstants.TYPE);
            String controlName = jsonObject.getString(TcFormControlConstants.LABEL);
            String description = jsonObject.getString(TcFormControlConstants.DESCRIPTION);
            Boolean required = jsonObject.getBoolean(TcFormControlConstants.REQUIRED);
            controlName = Boolean.FALSE.equals(required) ? controlName : ("*" + controlName);
            TcFormControlTypeEnum match = TcFormControlTypeEnum.match(type);
            switch (match) {
                case CUSTOMTABLE:
                    List<List<String>> customTableHead = getCustomTableHead(jsonObject, controlName);
                    list.addAll(customTableHead);
                    break;
                case INPUTNUMBER:
                    List<String> inputNumberHead = getInputNumberHead(jsonObject, controlName, description);
                    list.add(inputNumberHead);
                    break;
                case TAGS:
                    List<String> tagsHead = getTagsHead(controlName, description);
                    list.add(tagsHead);
                    break;
                case DATEPICKER:
                    List<String> datePickerHead = getDatePickerHead(jsonObject, controlName, description);
                    if (CollectionUtils.isNotEmpty(datePickerHead)) {
                        list.add(datePickerHead);
                    }
                    break;
                case MULTIPLESELECT:
                case CHECKBOX:
                case TREEMULTIPLESELECT:
                    List<String> multipleSelectHead = new ArrayList<>();
                    multipleSelectHead.add(TcFormControlConstants.EXCEL_HEAD_STATEMENT);
                    String name = jsonObject.getString(TcFormControlConstants.LABEL);
                    controlName = controlName + "\n（具体编码请参考【" + name + "】编码表，采用英文\",\"分隔）";
                    if (StringUtils.isNotBlank(description)) {
                        controlName = controlName + "\n（" + description + "）";
                    }
                    multipleSelectHead.add(controlName);
                    list.add(multipleSelectHead);
                    break;
                default:
                    List<String> defaultHead = new ArrayList<>();
                    defaultHead.add(TcFormControlConstants.EXCEL_HEAD_STATEMENT);
                    if (StringUtils.isNotBlank(description)) {
                        controlName = controlName + "\n（" + description + "）";
                    }
                    defaultHead.add(controlName);
                    list.add(defaultHead);
                    break;
            }
        }
        return list;
    }

    private static List<String> getTagsHead(String controlName, String description) {
        List<String> tagsHead = new ArrayList<>();
        tagsHead.add(TcFormControlConstants.EXCEL_HEAD_STATEMENT);
        controlName = controlName + "\n（使用英文,分隔标签）";
        controlName = StringUtils.isEmpty(description) ? controlName : (controlName + "\n（" + description + "）");
        tagsHead.add(controlName);
        return tagsHead;
    }

    private static List<String> getDatePickerHead(JSONObject jsonObject, String controlName, String description) {
        // 导出时间日期支持类型：日期选择、时间选择、时间日期选择、月份选择、年份选择
        JSONObject formatJson =
                jsonObject.getJSONObject(TcFormControlConstants.CHECK_OUT).getJSONObject(TcFormControlConstants.FORMAT);
        String value = formatJson.getString(TcFormControlConstants.VALUE);
        if (DATE_PICKER_TYPES.containsKey(value)) {
            controlName = StringUtils.isEmpty(description) ? controlName : (controlName + "\n（" + description + "）");
            List<String> datePickerHead = new ArrayList<>();
            datePickerHead.add(TcFormControlConstants.EXCEL_HEAD_STATEMENT);
            datePickerHead.add(controlName);
            return datePickerHead;
        }
        return Collections.emptyList();
    }

    private static List<String> getInputNumberHead(JSONObject jsonObject, String controlName, String description) {
        List<String> controlList = new ArrayList<>();
        controlList.add(TcFormControlConstants.EXCEL_HEAD_STATEMENT);
        String units = jsonObject.getString(TcFormControlConstants.UNIT);
        controlName = StringUtils.isEmpty(units) ? controlName : (controlName + "\n（单位：" + units + "）");
        controlName = StringUtils.isEmpty(description) ? controlName : (controlName + "\n（" + description + "）");
        controlList.add(controlName);
        return controlList;
    }

    private static List<List<String>> getCustomTableHead(JSONObject jsonObject, String controlName) {
        List<List<String>> list = new ArrayList<>();
        JSONArray childControlArray = jsonObject.getJSONArray(TcFormControlConstants.COLUMNS);
        List<JSONObject> childControlList = childControlArray.toJavaList(JSONObject.class);
        for (JSONObject child : childControlList) {
            List<String> customList = new ArrayList<>();
            customList.add(TcFormControlConstants.EXCEL_HEAD_STATEMENT);
            customList.add(controlName);
            String type = child.getString(TcFormControlConstants.TYPE);
            String childControlTitle = child.getString(TcFormControlConstants.TITLE);
            Boolean childRequired = child.getBoolean(TcFormControlConstants.REQUIRED);
            String childDescription = child.getString(TcFormControlConstants.DESCRIPTION);

            // 必填拼接
            if (Boolean.TRUE.equals(childRequired)) {
                childControlTitle = "*" + childControlTitle;
            }

            if (TcFormControlTypeEnum.MULTIPLESELECT.getName().equals(type)) {
                childControlTitle = childControlTitle + "\n（具体编码请参考【" + child.getString(TcFormControlConstants.TITLE) +
                        "】编码表，采用英文,分隔）";
            } else if (StringUtils.isNotBlank(childDescription)) {
                // 描述拼接
                childControlTitle = childControlTitle + "\n(" + childDescription + ")";
            }
            customList.add(childControlTitle);
            list.add(customList);
        }
        return list;
    }


    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框
     *
     * @param sheet    HSSFSheet sheetlist = wb.createSheet("XXXX");工作表对象
     * @param textList 下拉框内容
     * @param firstRow 添加下拉框对应开始行
     * @param endRow   添加下拉框对应结束行
     * @param firstCol 添加下拉框对应开始列
     * @param endCol   添加下拉框对应结束列
     * @return
     */
    private static XSSFSheet setHSSFValidation(XSSFSheet sheet, String[] textList, int firstRow, int endRow,
                                               int firstCol, int endCol) {
        // 这里将下拉框进行拆分存储在每一个单元格  并进行隐藏  防止下拉框数据过多存储在一个单元格的字符数大于255而报错
        XSSFWorkbook workbook = sheet.getWorkbook();
        Sheet hidden = workbook.createSheet("hidden");

        Cell cell;
        for (int i = 0, length = textList.length; i < length; i++) {
            String name = textList[i];
            Row roww = hidden.createRow(i);
            cell = roww.createCell(0);
            cell.setCellValue(name);
        }

        Name namedCell = workbook.createName();
        namedCell.setNameName("hidden");
        namedCell.setRefersToFormula("hidden!$A$1:$A$" + textList.length);
        // 将第二个sheet页设置为隐藏
        workbook.setSheetHidden(1, true);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("hidden");
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation validation = dvHelper.createValidation(dvConstraint, regions);
        validation.createErrorBox("错误提示", "您输入的内容，不符合限制条件。请选择下拉框之内的值。");
        if (validation instanceof XSSFDataValidation) {
            validation.setSuppressDropDownArrow(true);
            validation.setShowErrorBox(true);
        } else {
            validation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(validation);
        return sheet;
    }

    private static XSSFCellStyle setDefaultCellStyle(XSSFCellStyle cellStyle, Boolean useDefaultStyle) {
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        if (useDefaultStyle) {
            // 设置背景颜色
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // 设置底边框;
            cellStyle.setBorderBottom(BorderStyle.THIN);
            // 设置底边框颜色;
            cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            // 设置左边框;
            cellStyle.setBorderLeft(BorderStyle.THIN);
            // 设置左边框颜色;
            cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            // 设置右边框;
            cellStyle.setBorderRight(BorderStyle.THIN);
            // 设置右边框颜色;
            cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            // 设置顶边框;
            cellStyle.setBorderTop(BorderStyle.THIN);
            // 设置顶边框颜色;
            cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            // 设置水平居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            // 设置垂直居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            // 设置自动换行
            cellStyle.setWrapText(true);
        }
        return cellStyle;
    }

    private static XSSFFont setFontStyle(XSSFFont font) {
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        return font;
    }


}
