package com.soybeany.bdlib.basic.network.output.parser;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Json与DTO间的快捷转换工具类
 * <br> Created by Soybeany on 16/2/19.
 */
public class JsonParser {

    public static final Gson DEFAULT_GSON = new Gson();

    private JsonParser() {
    }

    /**
     * 转Dto（普通型）
     */
    public static <T> T json2Dto(@Nullable String json, @NonNull Class<T> aClass) {
        return json2Dto(json, aClass, DEFAULT_GSON);
    }

    /**
     * 转Dto（普通型）
     */
    public static <T> T json2Dto(@Nullable String json, @NonNull Class<T> aClass, @NonNull Gson gson) {
        return gson.fromJson(json, aClass);
    }

    /**
     * 转Dto（带泛型的）
     *
     * @param typeToken 参照 new TypeToken&lt;List&lt;User&gt;&gt;(){}
     */
    public static <T> T json2Dto(@Nullable String json, @NonNull TypeToken<T> typeToken) {
        return json2Dto(json, typeToken, DEFAULT_GSON);
    }

    /**
     * 转Dto（带泛型的）
     *
     * @param typeToken 参照 new TypeToken&lt;List&lt;User&gt;&gt;(){}
     */
    public static <T> T json2Dto(@Nullable String json, @NonNull TypeToken<T> typeToken, @NonNull Gson gson) {
        return gson.fromJson(json, typeToken.getType());
    }

    /**
     * 转Json（普通型）
     */
    public static String dto2Json(@Nullable Object object) {
        return dto2Json(object, DEFAULT_GSON);
    }

    /**
     * 转Json（普通型）
     */
    public static String dto2Json(@Nullable Object object, @NonNull Gson gson) {
        return gson.toJson(object);
    }

    /**
     * 获得自定义命名的gson对象<br>key:自定义的key，value:原key
     */
    public static Gson getFieldNamingGson(@NonNull final Map<String, String> nameMap) {
        return new GsonBuilder().setFieldNamingStrategy(new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                String originalName = nameMap.get(f.getName());
                if (null != originalName) {
                    return originalName;
                }
                return f.getName();
            }
        }).create();
    }

}
