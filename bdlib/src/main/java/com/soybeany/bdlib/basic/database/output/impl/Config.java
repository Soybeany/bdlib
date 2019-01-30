package com.soybeany.bdlib.basic.database.output.impl;


import com.soybeany.bdlib.basic.database.core.DBManageUtils;
import com.soybeany.bdlib.basic.database.core.DBSwitchUtils;
import com.soybeany.bdlib.basic.database.interfaces.IConfig;
import com.soybeany.bdlib.basic.database.interfaces.IEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * 数据库配置（默认实现）
 * <br>Created by Soybeany on 2017/1/17.
 */
public abstract class Config implements IConfig {

    private String mDBName; // 数据库名

    protected Config(String dbName) {
        Set<Class<? extends IEntity>> set = new HashSet<>();
        setupTables(set);
        DBManageUtils.setTables(mDBName = dbName, set);
    }

    @Override
    public String getDBName() {
        return mDBName;
    }

    @Override
    public void close() {
        DBSwitchUtils.closeDB(getDBName());
    }

    /**
     * 数据库表集合
     */
    protected abstract void setupTables(Set<Class<? extends IEntity>> tableSet);

}
