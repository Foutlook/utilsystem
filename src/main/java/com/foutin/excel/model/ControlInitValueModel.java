package com.foutin.excel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Description 控件初始值列表
 */
@AllArgsConstructor
@Data
public class ControlInitValueModel {

    /**
     * 控件类型
     */
    private String type;

    /**
     * 控件唯一标识
     */
    private String reactKey;

    /**
     * 初始值
     */
    private List<String> initValue;
}
