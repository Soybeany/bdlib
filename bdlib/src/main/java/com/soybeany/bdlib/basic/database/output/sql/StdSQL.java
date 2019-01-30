package com.soybeany.bdlib.basic.database.output.sql;


import com.soybeany.bdlib.basic.database.core.BaseSQL;
import com.soybeany.bdlib.basic.database.core.DBReflectionUtils;
import com.soybeany.bdlib.basic.database.interfaces.IEntity;

import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.EQUAL;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.IS;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.IS_NOT;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.NEGATE;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.NO_CASE;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.NULL;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.SELECT_SQLITE_MASTER;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.SQL_ADD_COLUMN;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.SQL_ALTER_TABLE;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.SQL_RENAME_TABLE;

/**
 * <br>Created by Soybeany on 2017/1/19.
 */
public class StdSQL extends BaseSQL {

    private StdSQL() {

    }

    /**
     * 等于值
     */
    public static String equal(String field, Object value) {
        return field + EQUAL + "'" + value + "'";
    }

    /**
     * 查询null或非null值
     */
    public static String nuLL(String field, boolean isNull) {
        return field + " " + (isNull ? IS : IS_NOT) + " " + NULL;
    }

    /**
     * 查询布尔值
     */
    public static String bool(String field, boolean isTrue) {
        return field + (isTrue ? NEGATE : "") + EQUAL + "0";
    }

    /**
     * 数据库表添加字段
     */
    public static String addColumn(Class<? extends IEntity> table, String fieldName) {
        return getOptionSQL(SQL_ALTER_TABLE, table, SQL_ADD_COLUMN + getDefinition(DBReflectionUtils.getTableField(table, fieldName)));
    }

    /**
     * 数据库表重命名
     */
    public static String renameTable(String oldName, Class<? extends IEntity> table) {
        return getOptionSQL(SQL_ALTER_TABLE + " " + oldName + " " + SQL_RENAME_TABLE, table, "");
    }

    /**
     * 查询数据库指定表是否存在指定列
     */
    public static String queryColumnExist(String tableName) {
        return SELECT_SQLITE_MASTER + " WHERE type = 'table' AND name = '" + tableName + "' " + NO_CASE;
    }

}
