package com.foutin.nio.netty.serialize.json;

import lombok.Data;

/**
 * @author f2485
 * @Description
 * @date 2024/12/20 16:50
 */
@Data
public class JsonMsg {

    private int id; //id Field(字段)
    private String content;//content Field(字段)
    //序列化：调用通用方法，使用谷歌 Gson 转成字符串
    public String convertToJson() {
        return JsonUtil.pojoToJson(this);
    }
    //反序列化：使用阿里 FastJson 转成 Java POJO 对象
    public static JsonMsg parseFromJson(String json) {
        return JsonUtil.jsonToPojo(json, JsonMsg.class);
    }
}
