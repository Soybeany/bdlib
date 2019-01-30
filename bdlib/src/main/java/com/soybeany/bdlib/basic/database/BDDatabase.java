package com.soybeany.bdlib.basic.database;

import android.content.Context;

import com.soybeany.bdlib.basic.database.core.DBHelper;
import com.soybeany.bdlib.basic.database.core.DBSwitchUtils;
import com.soybeany.bdlib.basic.database.interfaces.IConfig;
import com.soybeany.bdlib.basic.database.interfaces.IEntity;
import com.soybeany.bdlib.basic.database.interfaces.IMigration;
import com.soybeany.bdlib.basic.database.interfaces.ITransaction;
import com.soybeany.bdlib.basic.database.output.impl.Config;
import com.soybeany.bdlib.basic.database.output.manipulation.Delete;
import com.soybeany.bdlib.basic.database.output.manipulation.Insert;
import com.soybeany.bdlib.basic.database.output.manipulation.Query;
import com.soybeany.bdlib.basic.database.output.manipulation.Update;
import com.soybeany.bdlib.basic.database.output.sql.StdSQL;
import com.soybeany.bdlib.basic.database.util.DBLogUtils;
import com.soybeany.bdlib.util.async.AsyncUtils;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * 数据库模块（BD系列）
 * <br>优点：
 * <br>1.在切换数据库时，自动关闭上一数据库（可关闭此功能）
 * <br>2.使用数据库配置（{@link IConfig}）管理数据库，默认实现（{@link Config}）提供了根据数据库实体（{@link IEntity}）创建数据库表的方法
 * <br>3.需要添加新表时，只需在数据库配置默认实现（{@link Config}）中添加，以及增大相应数据库版本号即可，不需要使用数据库升级信息（{@link IMigration}）
 * <br>4.需要对已有表字段进行“增”操作时，使用数据库升级信息（{@link IMigration}）及SQL标准语句（{@link StdSQL}）可便捷完成
 * <p>
 * <br>基本功能：
 * <br>1.数据库的自动切换
 * <br>2.使用{@link IConfig}进行数据库设置，如需要创建的数据库名、版本号、数据库升级语句（增加已有表中的字段）
 * <br>3.使用{@link IMigration}进行数据库升级管理，
 * <p>
 * <br>便捷功能：
 * <br>1.可配套{@link AsyncUtils}使用，以实现异步操作
 * <br>Created by Soybeany on 2017/1/13.
 */
public class BDDatabase {

    private static BDDatabase mInstance = new BDDatabase(); // 单例

    private BDDatabase() {

    }


    // //////////////////////////////////静态方法区//////////////////////////////////

    /**
     * 获得单例
     */
    public static BDDatabase getInstance() {
        return mInstance;
    }

    /**
     * 关闭指定的数据库
     */
    public static void closeDB(String dbName) {
        DBSwitchUtils.closeDB(dbName);
    }

    /**
     * 关闭全部数据库（一般在Application的onTerminate中调用）
     */
    public static void closeAllDB() {
        DBSwitchUtils.closeAllDB();
    }

    /**
     * 使用事务
     */
    public static void useTransaction(IConfig dbConfig, ITransaction transaction) {
        DBSwitchUtils.useTransaction(dbConfig, transaction);
    }

    /**
     * 初始化数据库
     */
    public static void init(Context context) {
        SQLiteDatabase.loadLibs(context);
    }

    /**
     * 释放占用的资源（一般在Application的onTerminate中调用）
     */
    public static void release() {
        closeAllDB();
    }


    // //////////////////////////////////“增删改查”操作//////////////////////////////////

    /**
     * 增加记录
     */
    public static Insert insert() {
        return Insert.getInstance();
    }

    /**
     * 删除记录
     */
    public static Delete delete() {
        return Delete.getInstance();
    }

    /**
     * 修改记录
     */
    public static Update update() {
        return Update.getInstance();
    }

    /**
     * 查询记录
     */
    public static Query query() {
        return Query.getInstance();
    }


    // //////////////////////////////////自定义配置//////////////////////////////////

    /**
     * 需要日志输出
     */
    public BDDatabase needLog(boolean flag) {
        DBLogUtils.NEED_LGO = flag;
        return this;
    }

    /**
     * 切换数据库后不关闭上一个已开启的数据库
     */
    public BDDatabase keepDBOpen() {
        DBSwitchUtils.NEED_DB_CLOSE = false;
        return this;
    }

    /**
     * 设置数据库加密时使用的密码
     *
     * @param key 密码
     */
    public BDDatabase secretKey(String key) {
        DBHelper.KEY = key;
        return this;
    }

    /**
     * 需要使用加密
     */
    public BDDatabase needEncryption() {
        DBHelper.NEED_ENCRYPT = true;
        return this;
    }

    /**
     * 初始化指定数据库（适用于应用刚打开时的数据库初始化）
     */
    public BDDatabase initDBs(IConfig... dbConfigs) {
        DBSwitchUtils.initDBs(dbConfigs);
        return this;
    }

}
