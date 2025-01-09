package com.foutin.nio.netty.serialize.json;

import com.alibaba.fastjson.JSON;
import com.google.gson.GsonBuilder;

/**
 * @author f2485
 * @Description
 * @date 2024/12/20 16:51
 */
public class JsonUtil {


    // 谷歌 GsonBuilder 构造器
    static GsonBuilder gb = new GsonBuilder();

    static {
        // 不需要 html escape
        gb.disableHtmlEscaping();
    }

    // 序列化：使用谷歌 Gson 将 POJO 转成字符串
    public static String pojoToJson(java.lang.Object obj) {
        return gb.create().toJson(obj);
    }

    // 反序列化：使用阿里 Fastjson 将字符串转成 POJO 对象
    public static <T> T jsonToPojo(String json, Class<T> tClass) {
        return JSON.parseObject(json, tClass);
    }

}
