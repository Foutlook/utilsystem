package com.foutin.excel.model;

import lombok.Data;

import java.util.List;

/**
 * @author t2215
 */
@Data
public class DcFormOptionValueVO {
    /**
     * 选项名
     */
    private String label;
    /**
     * 选项值
     */
    private String value;

    /**
     * 选项值是否可选， true：不可选，false：可选
     * 默认false
     */
    private Boolean disabled;

    /**
     * 子节点
     */
    private List<DcFormOptionValueVO> children;

    public DcFormOptionValueVO(String label, String value, Boolean disabled) {
        this.label = label;
        this.value = value;
        this.disabled = disabled;
    }

    public DcFormOptionValueVO(String label, String value, Boolean disabled, List<DcFormOptionValueVO> children) {
        this.label = label;
        this.value = value;
        this.disabled = disabled;
        this.children = children;
    }

    public DcFormOptionValueVO() {
    }
}
