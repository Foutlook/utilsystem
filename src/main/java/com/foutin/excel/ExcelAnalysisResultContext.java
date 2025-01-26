package com.foutin.excel;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * excel解析上下环境
 *
 * @author f2485
 */
public final class ExcelAnalysisResultContext {

    /**
     * 存放分组后的 每行的列数据
     */
    private static final ThreadLocal<List<JSONObject>> contextModelThreadLocal = new ThreadLocal<>();

    private ExcelAnalysisResultContext() {
        // do nothing
    }

    public static void set(List<JSONObject> contextModel) {
        contextModelThreadLocal.set(contextModel);
    }

    public static List<JSONObject> get() {
        return Optional.ofNullable(contextModelThreadLocal.get()).orElse(new ArrayList<>());
    }

    public static void remove() {
        contextModelThreadLocal.remove();
    }

}
