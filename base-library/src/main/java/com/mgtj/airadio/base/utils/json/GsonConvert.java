package com.mgtj.airadio.base.utils.json;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * Gson Convert
 * ================================================
 */
public class GsonConvert {


    public static <T> T fromJson(byte[] jsonArray, Class<T> type) {
        String json = new String(jsonArray, StandardCharsets.UTF_8);
        return fromJson(json, type);
    }

    public static <T> T fromJson(byte[] jsonArray, Type type) {
        String json = new String(jsonArray, StandardCharsets.UTF_8);
        return fromJson(json, type);
    }

    public static <T> T fromJson(JsonReader reader, Type typeOfT) {
        return GsonUtils.getGson().fromJson(reader, typeOfT);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT) {
        return GsonUtils.getGson().fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return GsonUtils.fromJson(json, type);
    }

    public static <T> T fromJson(String json, Type type) {
        return GsonUtils.fromJson(json, type);
    }

    public static <T> T fromJson(Reader json, Class<T> classOfT) {
        return GsonUtils.fromJson(json, classOfT);
    }

    public static <T> T fromJson(Reader json, Type typeOfT) {
        return GsonUtils.fromJson(json, typeOfT);
    }

    public static String toJson(Object src) {
        return GsonUtils.toJson(src);
    }

    public static String toJson(Object src, Type typeOfSrc) {
        return GsonUtils.toJson(src, typeOfSrc);
    }

    public static <T> List<T> toJavaList(String json, Class<T> clazz) {
        JsonArray jsonArray = fromJson(json, JsonArray.class);
        List<T> list = new ArrayList<T>();
        if (jsonArray != null && jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                list.add(fromJson(jsonArray.get(i), clazz));
            }
        }
        return list;
    }

}