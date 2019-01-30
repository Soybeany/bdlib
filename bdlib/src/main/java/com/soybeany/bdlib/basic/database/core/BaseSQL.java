package com.soybeany.bdlib.basic.database.core;

import com.soybeany.bdlib.basic.database.annotation.Table;
import com.soybeany.bdlib.basic.database.core.enums.Operator;
import com.soybeany.bdlib.basic.database.interfaces.IEntity;
import com.soybeany.bdlib.basic.database.util.DBStringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.AUTOINCREMENT;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.DOT_SEPARATOR;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.EQUAL;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.FLOAT;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.GREATER;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.INTEGER;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.LEFT_BRACKET;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.LESS;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.NEGATE;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.PRIMARY_KEY;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.RIGHT_BRACKET;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.SQL_CREATE_TABLE;
import static com.soybeany.bdlib.basic.database.interfaces.ISQLEntity.TEXT;


/**
 * 基础SQL语言
 * <br>Created by Soybeany on 2017/1/7.
 */
public class BaseSQL {

    // //////////////////////////////////数据库表操作//////////////////////////////////

    /**
     * 创建数据库表
     */
    static String createTable(Class<? extends IEntity> table) {
        List<Field> fields = DBReflectionUtils.getTableFields(table);
        List<String> definitions = new ArrayList<>();
        String definition;
        for (Field field : fields) {
            definition = getDefinition(field);
            if (null != definition) {
                definitions.add(definition);
            }
        }
        return getOptionSQL(SQL_CREATE_TABLE, table, LEFT_BRACKET + DBStringUtils.join(definitions, DOT_SEPARATOR) + RIGHT_BRACKET);
    }

    /**
     * 获得指定操作的SQL语句
     */
    protected static String getOptionSQL(String option, Class<? extends IEntity> table, String supplement) {
        return option + " " + DBReflectionUtils.getTableName(table) + " " + supplement;
    }

    /**
     * 从指定的字段中获得描述
     */
    protected static String getDefinition(Field field) {
        Class<?> fieldType = field.getType();
        String fieldName = DBReflectionUtils.getColumnName(field);
        Integer fieldLength = DBReflectionUtils.getColumnLength(field);
        String definition = null;

        if (DBReflectionUtils.typeIsSQLiteFloat(fieldType)) {
            definition = fieldName + " " + FLOAT;
        } else if (DBReflectionUtils.typeIsSQLiteInteger(fieldType)) {
            definition = fieldName + " " + INTEGER;
        } else if (DBReflectionUtils.typeIsSQLiteString(fieldType)) {
            definition = fieldName + " " + TEXT;
        }

        if (definition != null) {
            // 添加长度定义
            if (fieldLength != null && fieldLength > 0) {
                definition = definition + LEFT_BRACKET + fieldLength + RIGHT_BRACKET;
            }
            // 添加主键、自增长定义
            if (Table.DEFAULT_ID_NAME.equals(fieldName)) {
                definition = definition + " " + PRIMARY_KEY + " " + AUTOINCREMENT;
            }
        }

        return definition;
    }

    // //////////////////////////////////数据库表查询//////////////////////////////////

    /**
     * 比较
     */
    public static String compare(String field, Operator operator, Object value) {

        // 获得运算符
        String operatorStr;
        switch (operator) {
            case eq:
                operatorStr = EQUAL;
                break;
            case lt:
                operatorStr = LESS;
                break;
            case gt:
                operatorStr = GREATER;
                break;
            case ne:
                operatorStr = NEGATE + EQUAL;
                break;
            case let:
                operatorStr = LESS + EQUAL;
                break;
            case get:
                operatorStr = GREATER + EQUAL;
                break;
            default:
                throw new RuntimeException("使用了不支持的运算符：" + operator);
        }

        // 获得值
        String valueStr;
        Class clazz = value.getClass();
        if (DBReflectionUtils.typeIsSQLiteInteger(clazz) || DBReflectionUtils.typeIsSQLiteFloat(clazz)) {
            valueStr = value.toString();
        } else {
            valueStr = "'" + value + "'";
        }

        return field + operatorStr + valueStr;
    }

    /**
     * 通过id进行查询
     */
    static String queryId(long id) {
        return compare(Table.DEFAULT_ID_NAME, Operator.eq, id);
    }

}
