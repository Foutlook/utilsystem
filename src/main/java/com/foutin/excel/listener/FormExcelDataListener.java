package com.foutin.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.foutin.excel.ExcelAnalysisResultContext;
import com.foutin.excel.ExcelUtils;
import com.foutin.excel.FormParseUtils;
import com.foutin.excel.SymbolConstants;
import com.foutin.excel.TcFormControlConstants;
import com.foutin.excel.TcFormControlTypeEnum;
import com.foutin.excel.model.DcFormOptionValueVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description 解析表单excel数据监听器
 */
@Slf4j
public class FormExcelDataListener extends AnalysisEventListener<Map<Integer, String>> {

    private final String mateInfo;

    /**
     * excel头数据
     * key-列索引，value-控件名，表格控件是map
     * 如果是表格时：key只会取表格子控件的第一列作为值
     */
    private final Map<Integer, Object> headColumnMap = new HashMap<>();

    /**
     * 表格列值索引map
     * key-表格名，value-列索引和子空间名map
     */
    private final Map<String, Map<Integer, String>> customControlHeadSameColumnMap = new HashMap<>();

    /**
     * 行数据分组 行索引
     */
    private final List<Integer> dataGroupRowIndex = new ArrayList<>();

    /**
     * 序号列值
     */
    private final Set<String> serialNumber = new HashSet<>();

    /**
     * 存放分组后的 每行的列数据
     * map：K-控件名  V-控件值
     */
    private final List<Map<String, List<String>>> dataGroupColumnValueMap = new ArrayList<>();


    public FormExcelDataListener(String mateInfo) {
        this.mateInfo = mateInfo;
    }

    /**
     * 这里会一行行的返回头
     *
     * @param headMap 每行 头数据
     * @param context 解析上下文
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        Integer rowIndex = context.readRowHolder().getRowIndex();
        // 解析表头并分组
        parseHeadGroup(headMap, rowIndex);
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        ReadSheetHolder readSheetHolder = context.readSheetHolder();
        Integer allRowNumber = Optional.ofNullable(readSheetHolder.getApproximateTotalRowNumber()).orElse(0);
        Integer headRowNumber = Optional.ofNullable(readSheetHolder.getHeadRowNumber()).orElse(0);
        int allDataRowNum = allRowNumber - headRowNumber;
        // AssertUtil.isFalse(allDataRowNum > 50000, "超出总行数50000条限制，当前总行数为：" + allDataRowNum);

        String serial = data.get(0);
        Integer rowIndex = context.readRowHolder().getRowIndex();
        if (rowIndex == 2 || (rowIndex == 3 && !customControlHeadSameColumnMap.isEmpty())) {
            // 无表格和有表格第一数据行必须存在序列
            serial = StringUtils.isNotBlank(serial) ? serial : "1";
        }

        if ((StringUtils.isNotBlank(serial) && serialNumber.add(serial))) {
            // 其他数据行 根据第一列编号分组
            dataGroupRowIndex.add(rowIndex);
            // 非第一行且第一列不为空 行放入map
            this.setRowDataToMap(data);
            // 记录序号列值
            String serialNum = StringUtils.isNotBlank(serial) ? serial : "1";
            serialNumber.add(serialNum);
        } else {
            // 非第一行且第一列为空 行放入map
            this.setRowDataToMap(data);
        }
    }

    /**
     * 保存行数据到 map中
     *
     * @param data excel行数据
     */
    private void setRowDataToMap(Map<Integer, String> data) {
        // 根据分组编号初始化map
        this.initDataGroupColumnValueMap(dataGroupColumnValueMap);

        // 获取当前组 对应的数据map
        Map<String, List<String>> rowDataGroupMap = dataGroupColumnValueMap.get(dataGroupRowIndex.size() - 1);

        // 表格头 列索引和控件名map
        AtomicReference<Map<Integer, String>> customTableHeadMap = new AtomicReference<>();
        // 遍历列数据
        data.forEach((columnIndex, value) -> {
            // 需要列不存入map
            if (columnIndex == 0) {
                return;
            }
            // 如果是表格控件 headColumnMap key只会取子控件的第一列
            Object headObject = headColumnMap.get(columnIndex);
            // 处理普通控件
            if (headObject instanceof String) {
                String headString = (String) headObject;
                this.handlerNormalRowDataValue(rowDataGroupMap, value, headString, false);
            }

            // 处理表格控件，下标存在customTableHeadMap中
            if (headObject instanceof Map) {
                customTableHeadMap.set((Map<Integer, String>) headObject);
                this.handlerCustomTableRowValue(rowDataGroupMap, customTableHeadMap, columnIndex, value);
            }
            // 处理表格控件,下标存在customTableHeadMap中
            if (null == headObject /*&& ObjectUtils.isNotEmpty(customTableHeadMap.get())*/) {
                this.handlerCustomTableRowValue(rowDataGroupMap, customTableHeadMap, columnIndex, value);
            }
        });
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

        List<JSONObject> supportControl = ExcelUtils.getSupportControl(mateInfo);
        // 获取所有支持控件名称分组map
        Map<String, JSONObject> controlJsonByName = getControlJsonByName(supportControl);

        // 获取下拉单选、下拉多选、单项选择、多项选择、树形单选、树形多选、级联多选控件对应选项值列表
        Map<String, List<DcFormOptionValueVO>> controlOptionValueMap =
                FormParseUtils.getControlOptionValue(supportControl, mateInfo);
        // 解析excel导入数据
        parseExcelImportControlData(controlOptionValueMap, controlJsonByName);
    }

    /**
     * 解析excel导入数据，获取控件选项值
     *
     * @param controlOptionValueMap 控件选项值map
     * @param controlJsonByName     支持控件名分组map
     */
    private void parseExcelImportControlData(Map<String, List<DcFormOptionValueVO>> controlOptionValueMap,
                                             Map<String, JSONObject> controlJsonByName) {
        List<JSONObject> excelControlValue = new ArrayList<>();
        for (Map<String, List<String>> columnValueMap : dataGroupColumnValueMap) {
            JSONObject controlValue = new JSONObject();

            for (Map.Entry<String, JSONObject> entry : controlJsonByName.entrySet()) {
                JSONObject value = entry.getValue();
                String type = value.getString(TcFormControlConstants.TYPE);
                String reactKey = value.getString(TcFormControlConstants.REACT_KEY);
                // 控件对应选项值
                List<DcFormOptionValueVO> optionValueList = controlOptionValueMap.get(reactKey);

                TcFormControlTypeEnum match = TcFormControlTypeEnum.match(type);
                switch (match) {
                    case CUSTOMTABLE:
                        setCustomTableOptionValue(value, controlOptionValueMap, columnValueMap, controlValue);
                        break;
                    case SELECT:
                    case RADIO:
                    case CHECKBOX:
                    case MULTIPLESELECT:
                        List<String> selectColumnLabelList = getControlColumnLabel(value, columnValueMap, true);
                        setSelectValue(value, optionValueList, selectColumnLabelList, controlValue);
                        break;
                    case TREESELECT:
                    case TREEMULTIPLESELECT:
                        List<String> treeColumnLabelList = getControlColumnLabel(value, columnValueMap, true);
                        setTreeValue(value, optionValueList, treeColumnLabelList, controlValue);
                        break;
                    case CASCADE:
                        List<String> cascadeColumnLabelList = getControlColumnLabel(value, columnValueMap, false);
                        setCascadeValue(value, optionValueList, cascadeColumnLabelList, controlValue);
                        break;
                    default:
                        List<String> valueList = getControlColumnLabel(value, columnValueMap, false);
                        if (CollectionUtils.isNotEmpty(valueList)) {
                            String innerKey = value.getString(TcFormControlConstants.INNER_KEY);
                            controlValue.put(innerKey, valueList.get(0));
                        }
                        break;
                }
            }
            excelControlValue.add(controlValue);
        }
        ExcelAnalysisResultContext.set(excelControlValue);
    }

    private void setCascadeValue(JSONObject control,
                                 List<DcFormOptionValueVO> controlOptionValueList,
                                 List<String> columnList, JSONObject controlValue) {
        if (CollectionUtils.isEmpty(columnList)) {
            return;
        }
        Map<String, String> treeOrCascadeOption = FormParseUtils.getTreeOrCascadeOption(controlOptionValueList, "",
                "", true);

        List<String> cascadeValue =
                treeOrCascadeOption.entrySet().stream().filter(o -> columnList.contains(o.getValue()))
                        .map(Map.Entry::getKey).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cascadeValue)) {
            return;
        }
        // 获取级联的所有innerKey
        List<String> innerKeys = control.getJSONArray(TcFormControlConstants.ATTRS).stream()
                .map(jsonObject -> ((JSONObject) jsonObject).getString(TcFormControlConstants.INNERKEY))
                .collect(Collectors.toList());


        String[] split = cascadeValue.get(0).split(SymbolConstants.SINGLE_SLASH);
        for (int i = 0; i < split.length; i++) {
            if (innerKeys.size() - 1 < i) {
                continue;
            }
            controlValue.put(innerKeys.get(i), split[i]);
        }
    }

    private void setTreeValue(JSONObject control,
                              List<DcFormOptionValueVO> controlOptionValueList,
                              List<String> columnList, JSONObject controlValue) {
        if (CollectionUtils.isEmpty(columnList)) {
            return;
        }
        Map<String, String> treeOrCascadeOption = FormParseUtils.getTreeOrCascadeOption(controlOptionValueList, "",
                "", true);

        List<String> treeValue =
                treeOrCascadeOption.entrySet().stream().filter(o -> columnList.contains(o.getValue()))
                        .map(v -> {
                            String[] split = v.getKey().split(SymbolConstants.SINGLE_SLASH);
                            return split[split.length - 1];
                        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(treeValue)) {
            return;
        }
        String innerKey = control.getString(TcFormControlConstants.INNER_KEY);
        String type = control.getString(TcFormControlConstants.TYPE);
        if (StringUtils.equalsAny(type, TcFormControlTypeEnum.TREEMULTIPLESELECT.getName())) {
            controlValue.put(innerKey, treeValue);
        } else {
            controlValue.put(innerKey, treeValue.get(0));
        }
    }

    /**
     * 获取控件对应的excel列值
     *
     * @param control        当前控件
     * @param columnValueMap 控件对应列值列表
     * @param isSplit        是否进行分割
     * @return 当前控件对应的实际列值
     */
    private List<String> getControlColumnLabel(JSONObject control, Map<String, List<String>> columnValueMap,
                                               boolean isSplit) {
        String label = control.getString(TcFormControlConstants.LABEL);
        List<String> valueList = columnValueMap.get(label);
        if (CollectionUtils.isEmpty(valueList)) {
            return Collections.emptyList();
        }
        if (isSplit) {
            String[] splitValue = valueList.get(0).split(SymbolConstants.COMMA);
            valueList = Arrays.stream(splitValue).collect(Collectors.toList());
        }
        return valueList;
    }

    private void setSelectValue(JSONObject control,
                                List<DcFormOptionValueVO> controlOptionValueList,
                                List<String> columnList, JSONObject controlValue) {
        if (CollectionUtils.isEmpty(columnList)) {
            return;
        }
        List<String> selectValue = controlOptionValueList.stream().filter(v -> columnList.contains(v.getLabel()))
                .map(DcFormOptionValueVO::getValue).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(selectValue)) {
            return;
        }
        String innerKey = control.getString(TcFormControlConstants.INNER_KEY);
        String type = control.getString(TcFormControlConstants.TYPE);
        if (StringUtils.equalsAny(type, TcFormControlTypeEnum.MULTIPLESELECT.getName(),
                TcFormControlTypeEnum.CHECKBOX.getName())) {
            controlValue.put(innerKey, selectValue);
        } else {
            controlValue.put(innerKey, selectValue.get(0));
        }

    }

    /**
     * 设置表格选项值
     *
     * @param control               当前控件
     * @param controlOptionValueMap 控件选项值map
     * @param columnValueMap        控件列值
     * @param controlValue          控件值
     */
    private void setCustomTableOptionValue(JSONObject control,
                                           Map<String, List<DcFormOptionValueVO>> controlOptionValueMap,
                                           Map<String, List<String>> columnValueMap,
                                           JSONObject controlValue) {
        String innerKey = control.getString(TcFormControlConstants.INNER_KEY);
        String customName = control.getString(TcFormControlConstants.LABEL);
        JSONArray childControlArray = control.getJSONArray(TcFormControlConstants.COLUMNS);
        List<JSONObject> childControlList = childControlArray.toJavaList(JSONObject.class);

        List<JSONObject> childControlValueList = new ArrayList<>();
        for (JSONObject child : childControlList) {
            String title =
                    TcFormControlConstants.EXCEL_CUSTOM_TABLE_HEAD + "-" + customName + "-" + child.getString(TcFormControlConstants.TITLE);
            List<String> controlLabelList = columnValueMap.get(title);
            if (CollectionUtils.isEmpty(controlLabelList)) {
                continue;
            }
            // 子控件对应的选项值
            setSubControlInnerValue(child, childControlValueList, controlLabelList, controlOptionValueMap);
        }
        if (CollectionUtils.isNotEmpty(childControlValueList)) {
            controlValue.put(innerKey, childControlValueList);
        }
    }

    /**
     * 子控件内部关键字和选项值
     *
     * @param subControl            子控件
     * @param childControlValueList 子控件只列表
     * @param controlLabelList      控件选项列表
     * @param controlOptionValueMap 控件选项值map
     */
    private void setSubControlInnerValue(JSONObject subControl,
                                         List<JSONObject> childControlValueList,
                                         List<String> controlLabelList,
                                         Map<String, List<DcFormOptionValueVO>> controlOptionValueMap) {
        String type = subControl.getString(TcFormControlConstants.TYPE);
        String subInnerKey = subControl.getString(TcFormControlConstants.DATA_INDEX);

        List<DcFormOptionValueVO> optionValueList;
        Map<String, String> selectOptionMap = null;
        if (StringUtils.equalsAny(type, TcFormControlTypeEnum.SELECT.getName(),
                TcFormControlTypeEnum.MULTIPLESELECT.getName())) {
            optionValueList = controlOptionValueMap.get(subControl.getString(TcFormControlConstants.KEY));
            selectOptionMap = FormParseUtils.getSelectOption(optionValueList);
        }
        if (TcFormControlTypeEnum.TREESELECT.getName().equals(type)) {
            optionValueList = controlOptionValueMap.get(subControl.getString(TcFormControlConstants.KEY));
            selectOptionMap = FormParseUtils.getTreeOrCascadeOption(optionValueList, "", "", true);
        }


        JSONObject jsonObject;
        for (int i = 0; i < controlLabelList.size(); i++) {
            if (childControlValueList.size() - 1 < i) {
                jsonObject = new JSONObject();
                childControlValueList.add(jsonObject);
            } else {
                jsonObject = childControlValueList.get(i);
            }

            String label = controlLabelList.get(i);
            if (StringUtils.isBlank(label)) {
                continue;
            }
            if (null != selectOptionMap && StringUtils.equalsAny(type, TcFormControlTypeEnum.SELECT.getName(),
                    TcFormControlTypeEnum.TREESELECT.getName())) {
                String value = selectOptionMap.entrySet().stream().filter(entry -> entry.getValue().equals(label))
                        .findFirst().map(Map.Entry::getKey).orElse(null);
                if (StringUtils.isNotBlank(value)) {
                    String[] split = value.split(SymbolConstants.SINGLE_SLASH);
                    jsonObject.put(subInnerKey, split[split.length - 1]);
                }
            } else if (null != selectOptionMap && StringUtils.equalsAny(type,
                    TcFormControlTypeEnum.MULTIPLESELECT.getName())) {
                List<String> labelList =
                        Arrays.stream(label.split(SymbolConstants.COMMA)).collect(Collectors.toList());
                List<String> valueList =
                        selectOptionMap.entrySet().stream()
                                .filter(entry -> labelList.contains(entry.getValue()))
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(valueList)) {
                    jsonObject.put(subInnerKey, valueList);
                }
            } else {
                jsonObject.put(subInnerKey, label);
            }
        }
    }


    /**
     * 通过控件名批量获取控件json
     *
     * @param allControlJson 支持控件
     * @return 控件名和对应控件关系map
     */
    private static Map<String, JSONObject> getControlJsonByName(List<JSONObject> allControlJson) {
        return allControlJson.stream().collect(Collectors.toMap(c -> c.getString(TcFormControlConstants.LABEL),
                Function.identity(), (o1, o2) -> o1));
    }

    /**
     * 处理表格行 控件值
     *
     * @param rowDataGroupMap    行对应的数据map
     * @param customTableHeadMap 表格头map
     * @param columnIndex        列索引
     * @param value              列索引对应的列值
     */
    private void handlerCustomTableRowValue(Map<String, List<String>> rowDataGroupMap,
                                            AtomicReference<Map<Integer, String>> customTableHeadMap,
                                            Integer columnIndex, String value) {
        String headString = customTableHeadMap.get().get(columnIndex);
        String subHeadString = null;
        for (Map.Entry<String, Map<Integer, String>> entry : customControlHeadSameColumnMap.entrySet()) {
            if (entry.getValue().containsKey(columnIndex)) {
                subHeadString =
                        TcFormControlConstants.EXCEL_CUSTOM_TABLE_HEAD + "-" + entry.getKey() + "-" + headString;
            }
        }
        // 处理表格子控件行数据
        handlerNormalRowDataValue(rowDataGroupMap, value, subHeadString, true);
    }

    /**
     * 处理普通控件行数据
     *
     * @param rowDataGroupMap 行对应的数据map
     * @param value           列对应的列值
     * @param subHeadString   控件名
     */
    private void handlerNormalRowDataValue(Map<String, List<String>> rowDataGroupMap, String value,
                                           String subHeadString, boolean isCustomTable) {
        List<String> valueString = rowDataGroupMap.get(subHeadString);
        if (CollectionUtils.isEmpty(valueString)) {
            valueString = new ArrayList<>();
            rowDataGroupMap.put(subHeadString, valueString);
        }
        if (StringUtils.isBlank(value) && isCustomTable) {
            valueString.add("");
        } else if (StringUtils.isNotBlank(value)) {
            valueString.add(value);
        }
    }

    /**
     * 初始化数据分组map
     */
    private void initDataGroupColumnValueMap(List<Map<String, List<String>>> dataGroupColumnValueMap) {
        int indexDif = dataGroupColumnValueMap.size();
        for (int i = indexDif; i < dataGroupRowIndex.size(); i++) {
            dataGroupColumnValueMap.add(i, new HashMap<>());
        }
    }


    /**
     * 解析表格头
     *
     * @param headMap  表格头数据
     * @param rowIndex 行号
     */
    private void parseHeadGroup(Map<Integer, ReadCellData<?>> headMap, Integer rowIndex) {
        // excel第一行是提示信息，不用解析
        if (rowIndex == 0) {
            return;
        }
        headMap.forEach((key, value) -> {
            String headString = value.getStringValue().split("\n")[0].replace("*", "");
            // 第一行查重
            if (rowIndex == 1) {
                Optional<Map.Entry<Integer, Object>> first =
                        headColumnMap.entrySet().stream().filter(h -> h.getValue().equals(headString)).findFirst();
                if (first.isPresent()) {
                    // 获取表格对应的列索引
                    Map<Integer, String> headSameColumnMap = customControlHeadSameColumnMap.computeIfAbsent(headString,
                            k -> new HashMap<>());
                    if (headSameColumnMap.isEmpty()) {
                        headSameColumnMap.put(first.get().getKey(), null);
                    }
                    headSameColumnMap.put(key, null);
                } else {
                    headColumnMap.put(key, headString);
                }
            } else if (rowIndex == 2) {
                // 第二行处理表格子控件, 用子控件名替换掉空字符串
                customControlHeadSameColumnMap.values().forEach(customTableMap -> {
                    if (!customTableMap.containsKey(key)) {
                        return;
                    }
                    customTableMap.put(key, headString);
                });
            }
        });


        // 第二行时 把子控件map替换表格位置
        if (rowIndex == 2 && !customControlHeadSameColumnMap.isEmpty()) {
            customControlHeadSameColumnMap.values().forEach(customTableMap -> {
                if (null != customTableMap && !customTableMap.isEmpty()) {
                    // 确保customTableMap的所有值都不为null
                    boolean containsNull = customTableMap.values().stream().anyMatch(StringUtils::isBlank);
                    if (containsNull) {
                        return;
                    }
                    // 对子Map的键进行升序排序
                    Integer firstKey = customTableMap.keySet().stream().sorted().findFirst().orElse(null);
                    headColumnMap.put(firstKey, customTableMap);
                }
            });
        }
    }
}
