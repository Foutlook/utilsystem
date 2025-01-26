package com.foutin.excel;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;


public enum TcFormControlTypeEnum {
    INPUT("INPUT", "单行文本", "varchar(128)", 128),
    TEXTAREA("TEXTAREA", "多行文本", "text", 3),
    INPUTNUMBER("INPUTNUMBER", "数字类型", "decimal(65,5)", 9),
    RADIO("RADIO", "单项选择", "varchar(32)", 32),
    CHECKBOX("CHECKBOX", "多项选择", "mediumtext", 4),
    SELECT("SELECT", "下拉单选", "varchar(32)", 32),
    MULTIPLESELECT("MULTIPLESELECT", "下拉多选", "varchar(128)", 0),
    TREESELECT("TREESELECT", "树形单选", "varchar(128)", 0),
    TREEMULTIPLESELECT("TREEMULTIPLESELECT", "树形多选", "varchar(128)", 0),
    CASCADE("CASCADE", "级联选择", "varchar(32)", 0),
    DATEPICKER("DATEPICKER", "日期时间", "varchar(50)", 50),
    DESCRIPTION("DESCRIPTION", "描述文字", "", 0),
    UPLOAD("UPLOAD", "文件上传", "varchar(128)", 128 + 4),
    MAP("MAP", "地址转换", "varchar(128)", 128 + 128),
    COORDINATE("COORDINATE", "坐标获取", "varchar(128)", 128 + 128),
    MAPAIR("MAPAIR", "数字地图", "varchar(128)", 128 + 128),
    GRID("GRID", "栅格布局", null, 0),
    DATETERM("DATETERM", "日期期限", "varchar(50)", 50),
    LINK("LINK", "跳转链接", "varchar(128)", 128),
    TAGS("TAGS", "标签文本", "varchar(128)", 128),
    CUSTOMTABLE("CUSTOMTABLE", "表格", "longtext", 0),
    SELECTREC("SELECTREC", "下拉值显", "varchar(32)", 32),
    CASCADEREC("CASCADEREC", "级联值显", "varchar(32)", 0),
    GROUP("GROUP", "分组", null, 0);

    private final String name;
    private final String description;

    /**
     * 默认mysql数据库类型
     */
    private final String defaultDbType;

    /**
     * 占用字符长度
     */
    private final int useCharacterLength;

    TcFormControlTypeEnum(String name, String description, String defaultDbType, int useCharacterLength) {
        this.name = name;
        this.description = description;
        this.defaultDbType = defaultDbType;
        this.useCharacterLength = useCharacterLength;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getDefaultDbType() {
        return defaultDbType;
    }

    public int getUseCharacterLength() {
        return useCharacterLength;
    }

    public static TcFormControlTypeEnum descriptionOf(String description) {
        for (TcFormControlTypeEnum e : values()) {
            if (StringUtils.equals(e.getDescription(), description)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown description:" + description);
    }

    /**
     * 控件类型转控件类型枚举
     *
     * @param type 控件类型
     * @return 控件类型枚举
     */
    public static TcFormControlTypeEnum getControlTypeEnum(String type) {
        if (!EnumUtils.isValidEnum(TcFormControlTypeEnum.class, type)) {
            throw new RuntimeException("不支持当前控件类型" + type);
        }
        return TcFormControlTypeEnum.valueOf(type);
    }

    /**
     * 根据控件类型获取数据库默认类型
     *
     * @param type 控件类型
     * @return 数据库默认类型
     */
    public static String getDefaultDbType(String type) {
        return getControlTypeEnum(type).getDefaultDbType();
    }

    public static TcFormControlTypeEnum matchType(String type) {
        for (TcFormControlTypeEnum tcFormControlTypeEnum : Arrays.asList(CUSTOMTABLE, GRID)) {
            if (tcFormControlTypeEnum.getName().equalsIgnoreCase(type)) {
                return tcFormControlTypeEnum;
            }
        }
        return match(type);
    }

    public static TcFormControlTypeEnum match(String type) {
        for (TcFormControlTypeEnum tcFormControlTypeEnum : TcFormControlTypeEnum.values()) {
            if (tcFormControlTypeEnum.getName().equalsIgnoreCase(type)) {
                return tcFormControlTypeEnum;
            }
        }
        return TcFormControlTypeEnum.INPUT;
    }

    public static String nameOf(String name) {
        for (TcFormControlTypeEnum value : TcFormControlTypeEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getDescription();
            }
        }
        return null;
    }
}
