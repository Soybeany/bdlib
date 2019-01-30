package com.soybeany.bdlib.basic.database.interfaces;

/**
 * 数据库配置接口，对应一个数据库
 * <br>Created by Soybeany on 2017/1/15.
 */
public interface IConfig {

    /**
     * 获得数据库名称
     */
    String getDBName();

    /**
     * 获得数据库版本号
     */
    int getDBVersion();

    /**
     * 获得升级时需要执行的sql语句
     */
    IMigration[] getMigrations();

    /**
     * 关闭此数据库
     */
    void close();

}
