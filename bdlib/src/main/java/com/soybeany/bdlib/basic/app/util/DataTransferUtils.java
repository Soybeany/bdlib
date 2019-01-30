package com.soybeany.bdlib.basic.app.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 * 数据传输工具类
 * <br>Created by Soybeany on 2017/3/14.
 */
public class DataTransferUtils {

    /**
     * 保存数据(适合Fragment)
     */
    public static void save(Fragment fragment, String key, Serializable content) {
        Bundle arguments = fragment.getArguments();
        if (null == arguments) {
            fragment.setArguments(arguments = new Bundle());
        }
        arguments.putSerializable(key, content);
    }

    /**
     * 保存数据(适合Activity)
     */
    public static void save(Intent intent, String key, Serializable content) {
        intent.putExtra(key, content);
    }

    /**
     * 读取数据(适合Fragment)
     */
    public static Serializable load(Fragment fragment, String key) {
        Bundle arguments = fragment.getArguments();
        if (null == arguments) {
            return null;
        }
        return arguments.getSerializable(key);
    }

    /**
     * 读取数据(适合Activity)
     */
    public static Serializable load(Intent intent, String key) {
        return intent.getSerializableExtra(key);
    }

}
