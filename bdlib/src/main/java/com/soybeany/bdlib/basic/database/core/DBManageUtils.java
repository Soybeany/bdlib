package com.soybeany.bdlib.basic.database.core;

import android.text.TextUtils;

import com.soybeany.bdlib.basic.database.interfaces.IConfig;
import com.soybeany.bdlib.basic.database.interfaces.IEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 数据库管理器
 * <br>Created by Soybeany on 2017/1/17.
 */
public class DBManageUtils {

    private static Map<String, Set<Class<? extends IEntity>>> TABLES_MAP = new HashMap<>();

    private DBManageUtils() {

    }

    /**
     * 获得全部数据库表
     */
    static Set<Class<? extends IEntity>> getTables(IConfig config) {
        return TABLES_MAP.get(config.getDBName());
    }

    /**
     * 设置数据库表
     */
    public static void setTables(String dbName, Set<Class<? extends IEntity>> tables) {
        if (TextUtils.isEmpty(dbName)) {
            throw new RuntimeException("数据库名不能为空");
        }
        TABLES_MAP.put(dbName, tables);
    }

}
