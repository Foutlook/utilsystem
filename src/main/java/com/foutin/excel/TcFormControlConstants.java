/*
 * ------------------------------------------------------------------
 * Copyright @ 2022 Hangzhou DtDream Technology Co.,Ltd. All rights reserved.
 * ------------------------------------------------------------------
 *       Product: JZDAS
 *   Module Name:
 *  Date Created: 2022/1/4
 *   Description:
 * ------------------------------------------------------------------
 * Modification History
 * DATE            Name           Description
 * ------------------------------------------------------------------
 * 2022/1/4      l2102
 * ------------------------------------------------------------------
 */
package com.foutin.excel;

/**
 * 表单控件常量
 *
 * @author l2102
 */
public final class TcFormControlConstants {

    public static final String ROW_DATA = "rowData";

    public static final String FORM_TITLE = "formTitle";

    /**
     * 内部关键字字段
     */
    public static final String ATTRS = "attrs";
    public static final String REC_ATTRS = "recAttrs";
    /**
     * 子节点
     */
    public static final String CHILDREN = "children";
    /**
     * 表格子节点
     */
    public static final String COLUMNS = "columns";

    /**
     * 控件通用字段-内部关键字key
     */
    public static final String INNER_KEY = "innerkey";

    /**
     * 文件名称
     */
    public static final String FILE_NAME_INNERKEY = "fileNameInnerkey";
    /**
     * 文件内容
     */
    public static final String FILE_CONTENT_INNERKEY = "fileContentInnerkey";
    /**
     * 地址名称
     */
    public static final String ADDRESS_NAME_INNERKEY = "addressNameInnerkey";
    /**
     * 地址经纬度
     */
    public static final String ADDRESS_COORNIDATE_INNERKEY = "addressCoornidateInnerkey";
    /**
     * 文件、地图内部关键字区分标识
     */
    public static final String CHECK_FLAG = "checkFlag";
    /**
     * 表格内部关键字
     */
    public static final String DATA_INDEX = "dataIndex";
    public static final String EXIST = "exist";
    /**
     * 表单数据key
     */
    public static final String RAW_DATA = "rowData";
    /**
     * checkout字段
     */
    public static final String CHECKOUT = "checkout";
    /**
     * 选项名
     */
    public static final String KEY = "key";
    public static final String FILE_TYPE = "fileType";
    public static final String REACT_KEY = "reactkey";
    public static final String DEPT_ID = "deptId";
    public static final String RULE_ID = "ruleId";
    /**
     * 级联层级
     */
    public static final String CASCADE_LEVEL = "cascadeLevel";


    /**
     * rowData相关属性
     */
    public static final String LABEL = "label";
    public static final String REC_LABEL = "recLabel";
    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String PLACEHOLDER = "placeholder";
    public static final String DESCRIPTION = "description";
    public static final String INNERKEY = "innerkey";
    public static final String READ_ONLY = "readOnly";
    public static final String REQUIRED = "required";
    public static final String HIDE = "hide";
    public static final String DEP_ID = "depId";
    public static final String DISABLED = "disabled";
    public static final String UNIQUEINTABLE = "uniqueInTable";
    public static final String UNIT = "unit";
    public static final String PRECISION = "precision";

    /**
     * 制表类型key
     */
    public static final String TAB_TYPE = "tabType";

    /**
     * 制表类型值
     */
    public static final String TAB_TYPE_PATH = "path";
    public static final String TAB_TYPE_NORMAL = "normal";
    public static final String TAB_TYPE_DICTIONARY = "dict";

    public static final String API_HTTP_PATH = "httpPath";
    /**
     * 选项
     */
    public static final String OPTS = "opts";

    public static final String INITIAL_VALUE = "initialValue";

    public static final String CHECK_OUT = "checkout";

    public static final String FORMAT = "format";
    /**
     * 自定义值
     */
    public static final String CUSTOM_VALUE = "customValue";

    /**
     * 时间属性相关
     */
    public static final String VALUE = "value";
    public static final String RANGE = "range";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";

    /**
     * 文本输入框
     */
    public static final String PATTERN = "pattern";
    public static final String MESSAGE = "message";
    public static final String LENGTH = "length";
    public static final String TEXTAREA = "TEXTAREA";
    public static final String MIN = "min";
    public static final String MAX = "max";

    /**
     * 字典
     */
    public static final String DICTIONARIES = "dictionaries";
    public static final String DICT_MAP_KEY = "dictMapKey";
    public static final String DIC_REF = "dicRef";
    public static final String DIC_TYPE = "dicType";
    public static final String DATA = "data";
    public static final String LIST = "list";
    public static final String CASCADE_TREE = "cascadeTree";

    /**
     * 联动
     */
    public static final String LINK_RULES = "linkRules";
    public static final String LINK_TYPE_VALUE = "linkTypeValue";
    public static final String LINK_RULE_CONFIG = "linkRuleConfig";
    public static final String LINK_TYPE = "linkType";

    /**
     * 接口选项值入参列表
     */
    public static final String INTERFACE_VALUE_PARAMS_LIST = "interfaceValueParamList";
    public static final String PATH_PARAMS = "pathParams";
    public static final String PATH_PARAMS_NAME = "name";
    public static final String PATH_PARAMS_VALUE = "reactkey";
    public static final String PATH_PARAMS_TYPE = "type";

    public static final String EXCEL_CUSTOM_TABLE_HEAD = "excelCustomTableHead";

    /**
     *
     * 表单导出excel填写说明
     */
    public static final String EXCEL_HEAD_STATEMENT = "1、按顺序对每份数据进行编号，如1、2、3……，编号不可重复。\n"
            + "2、当表单中存在表格字段时，一份数据可能包含多行，在每份数据的第一行填写编号，非表格字段同一编号下只读取第一行。\n"
            + "3、若不填写，默认与上面最近一行有编号的数据同属于一份数据。\n"
            + "4.导入前请删除示例数据，否则示例数据也会一并导入。";

    private TcFormControlConstants() {
        // do nothing
    }
}
