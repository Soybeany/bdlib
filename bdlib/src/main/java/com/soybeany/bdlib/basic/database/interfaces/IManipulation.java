package com.soybeany.bdlib.basic.database.interfaces;

import android.content.ContentValues;

import java.util.List;

/**
 * 数据库操作接口
 * <br>Created by Soybeany on 2017/1/16.
 */
public interface IManipulation {

    /**
     * 增加记录
     *
     * @return 行号
     */
    long insert(String tableName, ContentValues values);

    /**
     * 删除记录
     *
     * @return 影响的行数
     */
    int delete(String tableName, String where);

    /**
     * 通过id删除记录
     *
     * @return 影响的行数
     */
    int delete(String tableName, long id);

    /**
     * 更新记录
     *
     * @return 影响的行数
     */
    int update(String tableName, String where, ContentValues values);

    /**
     * 通过id更新记录
     *
     * @return 影响的行数
     */
    int update(String tableName, long id, ContentValues values);

    /**
     * 查询记录
     */
    <T extends IEntity> List<T> query(boolean distinct, Class<T> tableClass, String[] columns, String where, String groupBy, String having, String orderBy, String limit);

    /**
     * 通过id查询记录
     */
    <T extends IEntity> T queryId(Class<T> tableClass, long id);

    /**
     * 使用事务操作
     */
    void useTransaction(ITransaction transaction);
}
