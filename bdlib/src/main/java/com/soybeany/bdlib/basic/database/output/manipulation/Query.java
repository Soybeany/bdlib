package com.soybeany.bdlib.basic.database.output.manipulation;


import com.soybeany.bdlib.basic.database.core.DBSwitchUtils;
import com.soybeany.bdlib.basic.database.interfaces.IConfig;
import com.soybeany.bdlib.basic.database.interfaces.IEntity;
import com.soybeany.bdlib.basic.database.interfaces.ISQLEntity;

import java.util.List;

/**
 * <br>Created by Soybeany on 2017/1/17.
 */
public class Query {

    private static Query mInstance = new Query();

    private Query() {

    }


    // //////////////////////////////////静态方法//////////////////////////////////

    /**
     * 获得单例
     */
    public static Query getInstance() {
        return mInstance;
    }


    // //////////////////////////////////内部方法//////////////////////////////////

    /**
     * 获得单一实体
     */
    public static <T extends IEntity> T getSingle(List<T> list) {
        T entity = null;
        if (null != list && !list.isEmpty()) {
            entity = list.get(0);
        }
        return entity;
    }


    // //////////////////////////////////获得列表//////////////////////////////////

    /**
     * 使用完整方式（列表）
     */
    public <T extends IEntity> List<T> list(IConfig dBConfig, boolean distinct, Class<T> tableClass, String[] columns, String where, String groupBy, String having, String orderBy, String limit) {
        return DBSwitchUtils.getDBHelper(dBConfig).query(distinct, tableClass, columns, where, groupBy, having, orderBy, limit);
    }

    /**
     * 使用对象查询方式（列表）
     */
    public <T extends IEntity> List<T> list(IConfig dBConfig, Class<T> tableClass, ISQLEntity entity) {
        if (null != entity) {
            return list(dBConfig, entity.isDistinct(), tableClass, entity.getColumns(), entity.getWhere(), entity.getGroupBy(), entity.getHaving(), entity.getOrderBy(), entity.getLimit());
        } else {
            return list(dBConfig, false, tableClass, null, null, null, null, null, null);
        }
    }

    /**
     * 使用简易方式（列表）
     */
    public <T extends IEntity> List<T> list(IConfig dBConfig, Class<T> tableClass) {
        return list(dBConfig, tableClass, null);
    }


    // //////////////////////////////////获得单个对象//////////////////////////////////

    /**
     * 使用对象查询方式（单个）
     */
    public <T extends IEntity> T single(IConfig dBConfig, Class<T> tableClass, ISQLEntity entity) {
        return getSingle(list(dBConfig, tableClass, entity));
    }

    /**
     * 使用简易方式（单个）
     */
    public <T extends IEntity> T single(IConfig dBConfig, Class<T> tableClass) {
        return getSingle(list(dBConfig, tableClass));
    }

}
