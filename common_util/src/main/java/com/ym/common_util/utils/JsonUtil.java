package com.ym.common_util.utils;

import com.google.gson.Gson;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/24 20:58
 */
public class JsonUtil {
    private static Gson gson = new Gson();

    public static <T> String serialize(T object){
        return gson.toJson(object);
    }

    public static <T> T deserialize(String json,Class<T> clazz){
        return gson.fromJson(json,clazz);
    }
}
