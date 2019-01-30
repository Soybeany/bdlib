package com.soybeany.bdlib.basic.database.core;


import com.soybeany.bdlib.basic.database.interfaces.IConfig;
import com.soybeany.bdlib.basic.database.interfaces.IManipulation;
import com.soybeany.bdlib.basic.database.interfaces.ITransaction;
import com.soybeany.bdlib.basic.database.util.DBLogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库切换类
 * <br>Created by Soybeany on 2017/1/15.
 */
public class DBSwitchUtils {

    /**
     * 切换数据后是否需要关闭上一个使用的数据库
     */
    public static boolean NEED_DB_CLOSE = true;

    private static DBHelper CUR_DB_HELPER; // 当前连接的数据库辅助类
    private static Map<String, DBHelper> DB_HELPER_MAP = new HashMap<>(); // 数据库操作类映射，用于保证数据库连接的单例

    private DBSwitchUtils() {

    }

    /**
     * 初始化数据库
     */
    public static void initDBs(IConfig... dbConfigs) {
        for (IConfig dbConfig : dbConfigs) {
            getDBHelper(dbConfig);
        }
    }

    /**
     * 获得指定名称的数据库辅助类
     */
    public static IManipulation getDBHelper(IConfig dbConfig) {
        if (null == dbConfig) {
            throw new RuntimeException("IDBConfig不能传入null值");
        }
        String dbName = dbConfig.getDBName();
        DBHelper helper = DB_HELPER_MAP.get(dbName);
        if (null == helper) {
            helper = new DBHelper(dbConfig);
            DB_HELPER_MAP.put(dbName, helper);
        }
        switchCurDB(helper);
        return helper;
    }

    /**
     * 关闭指定数据库
     */
    public static void closeDB(String dbName) {
        DBHelper dbHelper = DB_HELPER_MAP.get(dbName);
        if (dbHelper == CUR_DB_HELPER) {
            closeCurDB();
        } else {
            closeDB(dbHelper);
        }
    }

    /**
     * 关闭全部数据库
     */
    public static void closeAllDB() {
        for (String dbName : DB_HELPER_MAP.keySet()) {
            closeDB(DB_HELPER_MAP.get(dbName));
        }
        closeCurDB();
    }

    /**
     * 使用事务
     */
    public static void useTransaction(IConfig dbConfig, ITransaction transaction) {
        getDBHelper(dbConfig).useTransaction(transaction);
    }

    /**
     * 切换当前使用的数据库为指定的数据库
     */
    private static void switchCurDB(DBHelper helper) {
        String curDBName = null;
        // 若之前已有一个数据库的连接
        if (null != CUR_DB_HELPER) {
            curDBName = CUR_DB_HELPER.getName();
            boolean isTheSameDB = (helper == CUR_DB_HELPER);
            if (!isTheSameDB) {
                //若不为同一个数据库连接，则按需对其关闭
                if (NEED_DB_CLOSE) {
                    closeCurDB();
                }
            } else {
                // 若为同一个数据库连接，则直接返回
                return;
            }
        }

        // 替换当前数据库连接
        DBLogUtils.i("切换数据库（" + curDBName + "->" + helper.getName() + "）");
        CUR_DB_HELPER = helper;
    }

    /**
     * 关闭当前使用的数据库
     */
    private static void closeCurDB() {
        closeDB(CUR_DB_HELPER);
        CUR_DB_HELPER = null;
    }

    /**
     * 关闭指定数据库
     */
    private static void closeDB(DBHelper helper) {
        if (null != helper) {
            helper.close();
            DBLogUtils.i("关闭数据库（" + helper.getName() + "）");
        }
    }
}
