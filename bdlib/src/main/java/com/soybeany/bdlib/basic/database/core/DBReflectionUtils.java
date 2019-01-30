package com.soybeany.bdlib.basic.database.core;

import android.content.ContentValues;
import android.database.Cursor;

import com.soybeany.bdlib.basic.database.annotation.Column;
import com.soybeany.bdlib.basic.database.annotation.Table;
import com.soybeany.bdlib.basic.database.interfaces.IConfig;
import com.soybeany.bdlib.basic.database.interfaces.IEntity;
import com.soybeany.bdlib.basic.database.interfaces.IManipulation;
import com.soybeany.bdlib.basic.database.util.DBLogUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DBReflectionUtils {

    private DBReflectionUtils() {

    }

    // //////////////////////////////////从注解中获得数据库表信息//////////////////////////////////

    /**
     * 获得表名
     */
    public static String getTableName(Class<? extends IEntity> type) {
        String tableName;
        Table annotation = type.getAnnotation(Table.class);
        if (annotation != null) {
            tableName = annotation.name();
        } else {
            tableName = type.getSimpleName();
        }
        return tableName;
    }

    /**
     * 获得数据库指定字段
     */
    public static Field getTableField(Class<? extends IEntity> type, String fieldName) {
        Field field;
        String errMsg;
        try {
            if ((field = type.getDeclaredField(fieldName)).isAnnotationPresent(Column.class)) {
                return field;
            }
            errMsg = "此字段没有添加“Column”注解";
        } catch (NoSuchFieldException e) {
            errMsg = "没有找到字段“" + fieldName + "”";
        }
        throw new RuntimeException(errMsg);
    }

    /**
     * 获得数据库字段
     */
    @SuppressWarnings("unchecked")
    static List<Field> getTableFields(Class<? extends IEntity> type) {
        List<Field> typeFields = new ArrayList<>();

        // 添加默认的自增长ID字段
        typeFields.add(getTableField((Class<? extends IEntity>) type.getSuperclass(), IEntity.DEFAULT_ID_FIELD));

        // 添加其它字段
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                typeFields.add(field);
            }
        }

        return typeFields;
    }

    /**
     * 获得列名
     */
    static String getColumnName(Field field) {
        Column annotation = field.getAnnotation(Column.class);
        if (annotation != null) {
            String name = annotation.name();
            if (!"".equals(name)) {
                return annotation.name();
            } else {
                return field.getName();
            }
        }
        return null;
    }

    /**
     * 获得列长度
     */
    static Integer getColumnLength(Field field) {
        Integer retVal = null;
        Column annotation = field.getAnnotation(Column.class);
        if (annotation != null) {
            int length = annotation.length();
            if (length > -1) {
                retVal = length;
            }
        }
        return retVal;
    }

    // //////////////////////////////////将值读写到被反射对象中//////////////////////////////////


    /**
     * 获得内容值
     */
    public static <T extends IEntity> ContentValues getContentValues(T entity) {
        ContentValues values = new ContentValues();

        for (Field field : getTableFields(entity.getClass())) {
            String fieldName = getColumnName(field);
            Class<?> fieldType = field.getType();

            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (isBoolean(fieldType)) {
                    values.put(fieldName, (Boolean) value);
                } else if (isUtilDate(fieldType)) {
                    values.put(fieldName, null != value ? ((java.util.Date) value).getTime() : null);
                } else if (isSqlDate(fieldType)) {
                    values.put(fieldName, null != value ? ((java.sql.Date) value).getTime() : null);
                } else if (isDouble(fieldType)) {
                    values.put(fieldName, (Double) value);
                } else if (isFloat(fieldType)) {
                    values.put(fieldName, (Float) value);
                } else if (isInteger(fieldType)) {
                    values.put(fieldName, (Integer) value);
                } else if (isLong(fieldType)) {
                    values.put(fieldName, (Long) value);
                } else if (typeIsSQLiteString(fieldType)) {
                    values.put(fieldName, null != value ? value.toString() : null);
                } else if (isEntity(fieldType)) {
                    Long entityId = null != value ? ((IEntity) value).save() : null;
                    values.put(fieldName, entityId);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                DBLogUtils.e(e.getMessage());
            }
        }
        return values;
    }

    /**
     * 处理数据指针
     */
    static <T extends IEntity> List<T> processCursor(IManipulation manipulation, IConfig dBConfig, Class<T> entityClass, Cursor cursor) {
        List<T> entities = new ArrayList<>();
        try {
            if (null != cursor && cursor.moveToFirst()) {
                do {
                    T entity = entityClass.newInstance();
                    entity.dbConfig(dBConfig);
                    loadFromCursor(manipulation, entity, cursor);
                    entities.add(entity);
                }
                while (cursor.moveToNext());
            }
        } catch (Exception e) {
            DBLogUtils.e(e.getMessage());
        }
        return entities;
    }

    /**
     * 从数据指针中赋值到实体
     */
    @SuppressWarnings("unchecked")
    private static <T extends IEntity> void loadFromCursor(IManipulation manipulation, T entity, Cursor cursor) {
        for (Field field : getTableFields(entity.getClass())) {
            String fieldName = getColumnName(field);
            Class<?> fieldType = field.getType();
            int columnIndex = cursor.getColumnIndex(fieldName);

            if (columnIndex < 0) {
                continue;
            }

            field.setAccessible(true);
            try {
                if (isEntity(fieldType)) {
                    IEntity otherEntity = manipulation.queryId((Class<IEntity>) fieldType, cursor.getLong(columnIndex));
                    if (null != otherEntity) {
                        field.set(entity, otherEntity);
                    }
                } else if (isBoolean(fieldType)) {
                    field.set(entity, cursor.getInt(columnIndex) != 0);
                } else if (isChar(fieldType)) {
                    field.set(entity, cursor.getString(columnIndex).charAt(0));
                } else if (isUtilDate(fieldType)) {
                    field.set(entity, new java.util.Date(cursor.getLong(columnIndex)));
                } else if (isSqlDate(fieldType)) {
                    field.set(entity, new java.sql.Date(cursor.getLong(columnIndex)));
                } else if (isDouble(fieldType)) {
                    field.set(entity, cursor.getDouble(columnIndex));
                } else if (isFloat(fieldType)) {
                    field.set(entity, cursor.getFloat(columnIndex));
                } else if (isInteger(fieldType)) {
                    field.set(entity, cursor.getInt(columnIndex));
                } else if (isLong(fieldType)) {
                    field.set(entity, cursor.getLong(columnIndex));
                } else if (isString(fieldType)) {
                    field.set(entity, cursor.getString(columnIndex));
                }
            } catch (Exception e) {
                DBLogUtils.e(e.getMessage());
            }
        }
    }


    // //////////////////////////////////高级类型判断//////////////////////////////////

    /**
     * 判断是否为浮点类型
     */
    static boolean typeIsSQLiteFloat(Class<?> type) {
        return isFloat(type) ||
                isDouble(type);
    }

    /**
     * 判断是否为整数类型
     */
    static boolean typeIsSQLiteInteger(Class<?> type) {
        return isBoolean(type) ||
                isInteger(type) ||
                isLong(type) ||
                isUtilDate(type) ||
                isSqlDate(type) ||
                isEntity(type);
    }

    /**
     * 判断是否为字符串类型
     */
    static boolean typeIsSQLiteString(Class<?> type) {
        return isChar(type) || isString(type);
    }


    // //////////////////////////////////基本类型判断//////////////////////////////////

    /**
     * 是否为布尔值
     */
    private static boolean isBoolean(Class<?> type) {
        return type.equals(Boolean.class) || type.equals(Boolean.TYPE);
    }

    /**
     * 是否为整型
     */
    private static boolean isInteger(Class<?> type) {
        return type.equals(Integer.class) || type.equals(Integer.TYPE);
    }

    /**
     * 是否为长整型
     */
    private static boolean isLong(Class<?> type) {
        return type.equals(Long.class) || type.equals(Long.TYPE);
    }

    /**
     * 是否为单精度浮点
     */
    private static boolean isFloat(Class<?> type) {
        return type.equals(Float.class) || type.equals(Float.TYPE);
    }

    /**
     * 是否为双精度浮点
     */
    private static boolean isDouble(Class<?> type) {
        return type.equals(Double.class) || type.equals(Double.TYPE);
    }

    /**
     * 是否为字符
     */
    private static boolean isChar(Class<?> type) {
        return type.equals(Character.TYPE);
    }

    /**
     * 是否为字符串
     */
    private static boolean isString(Class<?> type) {
        return type.equals(String.class);
    }

    /**
     * 是否为工具类中的日期
     */
    private static boolean isUtilDate(Class<?> type) {
        return type.equals(java.util.Date.class);
    }

    /**
     * 是否为sql中的日期
     */
    private static boolean isSqlDate(Class<?> type) {
        return type.equals(java.sql.Date.class);
    }

    /**
     * 是否为实体
     */
    private static boolean isEntity(Class<?> type) {
        Class<?> superClass;
        if (!type.isPrimitive() && null != (superClass = type.getSuperclass())) {
            for (Class<?> interfaceClass : superClass.getInterfaces()) {
                if (interfaceClass.equals(IEntity.class)) {
                    return true;
                }
            }
        }
        return false;
    }
}
