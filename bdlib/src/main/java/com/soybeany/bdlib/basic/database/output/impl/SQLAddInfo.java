package com.soybeany.bdlib.basic.database.output.impl;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.basic.database.interfaces.ISQLEntity;
import com.soybeany.bdlib.basic.database.interfaces.ISQLInfo;

/**
 * <br>Created by Soybeany on 2018/1/18.
 */
public class SQLAddInfo implements ISQLInfo {

    private String mTableName; // 表名
    private String mColumnName; // 列名
    private String mType; // 类型

    public SQLAddInfo(String tableName, String columnName, String type) {
        mTableName = tableName;
        mColumnName = columnName;
        mType = type;
    }

    @Override
    public String getTableName() {
        return mTableName;
    }

    @Override
    public boolean isNecessary(@Nullable String createSql) {
        return null != createSql && !createSql.toLowerCase().contains(mColumnName.toLowerCase());
    }

    @Override
    public String build() {
        return ISQLEntity.SQL_ALTER_TABLE + " " + mTableName + " "
                + ISQLEntity.SQL_ADD_COLUMN + " " + mColumnName + " " + mType;
    }

}
