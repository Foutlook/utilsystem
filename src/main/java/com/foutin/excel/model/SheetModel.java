package com.foutin.excel.model;

import com.alibaba.excel.write.handler.WriteHandler;
import lombok.Data;

import java.util.List;


@Data
public class SheetModel {

    /**
     * sheet页名称
     */
    private String sheetName;

    /**
     * 表头
     */
    private List<List<String>> headList;

    /**
     * 写excel拦截器
     */
    private List<WriteHandler> writeHandlers;

}
