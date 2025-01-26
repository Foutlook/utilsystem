package com.foutin.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.foutin.excel.model.ControlInitValueModel;
import com.foutin.excel.model.DcFormOptionValueVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 表单元数据解析工具，通用化解析方法
 */
public class FormParseUtils {

    private FormParseUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取存在初始值且非只读控件列表
     *
     * @param controlJson 控件JSON列表
     * @return 存在初始值且非只读控件列表
     */
    public static List<JSONObject> getDefaultValueControl(List<JSONObject> controlJson,
                                                          List<ControlInitValueModel> initValueModels) {
        List<JSONObject> defaultValueControl = new ArrayList<>();
        for (JSONObject control : controlJson) {
            String type = control.getString(TcFormControlConstants.TYPE);
            if (Boolean.TRUE.equals(control.getBoolean(TcFormControlConstants.READ_ONLY))) {
                continue;
            }

            if (TcFormControlTypeEnum.CUSTOMTABLE.getName().equals(type)) {
                JSONArray childControlArray = control.getJSONArray(TcFormControlConstants.COLUMNS);
                List<JSONObject> childControlList = childControlArray.toJavaList(JSONObject.class);
                // 递归查询
                List<JSONObject> exitSubControl = getDefaultValueControl(childControlList, initValueModels);
                if (CollectionUtils.isNotEmpty(exitSubControl)) {
                    control.put(TcFormControlConstants.COLUMNS, exitSubControl);
                    defaultValueControl.add(control);
                }
            } else if (StringUtils.equalsAny(type, TcFormControlTypeEnum.CHECKBOX.getName(),
                    TcFormControlTypeEnum.MULTIPLESELECT.getName(),
                    TcFormControlTypeEnum.TAGS.getName(),
                    TcFormControlTypeEnum.TREEMULTIPLESELECT.getName())) {
                JSONArray jsonArray = control.getJSONArray(TcFormControlConstants.INITIAL_VALUE);
                if (null != jsonArray && !jsonArray.isEmpty()) {
                    defaultValueControl.add(control);
                    if (null != initValueModels) {
                        String reactKey = control.getString(TcFormControlConstants.REACT_KEY);
                        if (StringUtils.isBlank(reactKey)) {
                            reactKey = control.getString(TcFormControlConstants.KEY);
                        }
                        List<String> value = jsonArray.toJavaList(String.class);
                        ControlInitValueModel initValueModel = new ControlInitValueModel(type, reactKey, value);
                        initValueModels.add(initValueModel);
                    }
                }
            } else if (TcFormControlTypeEnum.CASCADE.getName().equals(type)) {
                JSONArray jsonArray = control.getJSONArray(TcFormControlConstants.INITIAL_VALUE);
                if (null != jsonArray && !jsonArray.isEmpty()) {
                    defaultValueControl.add(control);
                    if (null != initValueModels) {
                        String reactKey = control.getString(TcFormControlConstants.REACT_KEY);
                        List<String> value = jsonArray.toJavaList(String.class);
                        ControlInitValueModel initValueModel = new ControlInitValueModel(type, reactKey, value);
                        initValueModels.add(initValueModel);
                    }
                }
            } else if (StringUtils.isNotBlank(control.getString(TcFormControlConstants.INITIAL_VALUE))) {
                defaultValueControl.add(control);
                if (null != initValueModels) {
                    String reactKey = control.getString(TcFormControlConstants.REACT_KEY);
                    if (StringUtils.isBlank(reactKey)) {
                        reactKey = control.getString(TcFormControlConstants.KEY);
                    }
                    String value = control.getString(TcFormControlConstants.INITIAL_VALUE);
                    ControlInitValueModel initValueModel = new ControlInitValueModel(type, reactKey,
                            Collections.singletonList(value));
                    initValueModels.add(initValueModel);
                }
            }
        }
        return defaultValueControl;
    }


    /**
     * 获取树形或级联控件的选项值
     *
     * @param optList     选项值列表
     * @param label       选项名
     * @param value       选项值
     * @param includeFlag false-跳过子节点，true-包含子节点
     * @return 存储value和label的map   K-选项值  V-选项名
     */
    public static Map<String, String> getTreeOrCascadeOption(List<DcFormOptionValueVO> optList, String label,
                                                             String value,
                                                             boolean includeFlag) {
        Map<String, String> labelValueMap = new LinkedHashMap<>();
        // 递归解析选项值
        recursiveTreeOrCascadeOption(optList, label, value, labelValueMap, includeFlag);
        return labelValueMap;
    }

    /**
     * 递归树形或级联控件的选项值
     *
     * @param optList       选项值列表
     * @param label         选项名
     * @param value         选项值
     * @param labelValueMap 存储value和label的map   K-选项值  V-选项名
     * @param includeFlag   false-跳过子节点，true-包含子节点
     */
    private static void recursiveTreeOrCascadeOption(List<DcFormOptionValueVO> optList, String label,
                                                     String value, Map<String, String> labelValueMap,
                                                     boolean includeFlag) {
        for (DcFormOptionValueVO opt : optList) {
            Boolean disabled = opt.getDisabled();
            // 级联父节点不可选，子节点也不可选。树形选择子父节点相互不影响
            if (Boolean.TRUE.equals(disabled) && Boolean.FALSE.equals(includeFlag)) {
                continue;
            }

            String childLabelName = label + opt.getLabel();
            String childValue = value + opt.getValue();
            labelValueMap.put(childValue, childLabelName);

            List<DcFormOptionValueVO> children = opt.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                recursiveTreeOrCascadeOption(children, childLabelName + SymbolConstants.SINGLE_SLASH,
                        childValue + SymbolConstants.SINGLE_SLASH, labelValueMap, includeFlag);
            }
        }
    }

    /**
     * 获取选择控件的选项值
     *
     * @param optList 选项值列表
     * @return 选择控件的value和label的map  K-选项值  V-选项名
     */
    public static Map<String, String> getSelectOption(List<DcFormOptionValueVO> optList) {
        Map<String, String> labelValueMap = new LinkedHashMap<>();
        for (DcFormOptionValueVO opt : optList) {
            if (Boolean.TRUE.equals(opt.getDisabled())) {
                continue;
            }
            String childValue = opt.getValue();
            String childLabel = opt.getLabel();
            labelValueMap.put(childValue, childLabel);
        }
        return labelValueMap;
    }


    /**
     * 获取控件选择值列表
     *
     * @param controlJson 控件json
     * @param metaInfo    元数据
     * @return 控件选择值列表 K-reactKey,V-选择值列表
     */
    public static Map<String, List<DcFormOptionValueVO>> getControlOptionValue(List<JSONObject> controlJson,
                                                                               String metaInfo) {
        Map<String, List<DcFormOptionValueVO>> optionValueMap = new HashMap<>();
        for (JSONObject control : controlJson) {
            String type = control.getString(TcFormControlConstants.TYPE);
            if (Boolean.FALSE.equals(checkType(type))) {
                continue;
            }

            // 获取控件选择值列表
            if (TcFormControlTypeEnum.CUSTOMTABLE.getName().equals(type)) {
                JSONArray childControlArray = control.getJSONArray(TcFormControlConstants.COLUMNS);
                List<JSONObject> childControlList = childControlArray.toJavaList(JSONObject.class);
                childControlList.forEach(childControl -> {
                    String childType = childControl.getString(TcFormControlConstants.TYPE);
                    if (StringUtils.equalsAny(childType, TcFormControlTypeEnum.SELECT.getName(),
                            TcFormControlTypeEnum.MULTIPLESELECT.getName(),
                            TcFormControlTypeEnum.TREESELECT.getName())) {
                        String reactKey = childControl.getString(TcFormControlConstants.KEY);
                        String innerKey = childControl.getString(TcFormControlConstants.DATA_INDEX);
                        // List<DcFormOptionValueVO> optList = TcCommonUtils.getOptionListByInnerKey(metaInfo,
                        //         innerKey, null, null);

                        List<DcFormOptionValueVO> optList = null;

                        optionValueMap.put(reactKey, optList);
                    }
                });
            } else if (TcFormControlTypeEnum.CASCADE.getName().equals(type)) {
                // 级联
                String reactKey = control.getString(TcFormControlConstants.REACT_KEY);
                // List<String> attrsInnerKey = TcCommonUtils.getAttrsInnerKey(control);
                List<String> attrsInnerKey = null;
                // List<DcFormOptionValueVO> optList = TcCommonUtils.getOptionListByInnerKey(metaInfo,
                //         attrsInnerKey.get(0), null, null);

                List<DcFormOptionValueVO> optList = null;
                optionValueMap.put(reactKey, optList);
            } else {
                String reactKey = control.getString(TcFormControlConstants.REACT_KEY);
                String innerKey = control.getString(TcFormControlConstants.INNER_KEY);
                // List<DcFormOptionValueVO> optList = TcCommonUtils.getOptionListByInnerKey(metaInfo,
                //         innerKey, null, null);
                List<DcFormOptionValueVO> optList = null;
                optionValueMap.put(reactKey, optList);
            }
        }
        return optionValueMap;
    }

    public static boolean checkType(String type) {
        return StringUtils.equalsAny(type, TcFormControlTypeEnum.SELECT.getName(),
                TcFormControlTypeEnum.RADIO.getName(), TcFormControlTypeEnum.CHECKBOX.getName(),
                TcFormControlTypeEnum.MULTIPLESELECT.getName(), TcFormControlTypeEnum.TREESELECT.getName(),
                TcFormControlTypeEnum.TREEMULTIPLESELECT.getName(), TcFormControlTypeEnum.CASCADE.getName(),
                TcFormControlTypeEnum.CUSTOMTABLE.getName());
    }

}
