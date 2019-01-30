package com.soybeany.bdlib.basic.database.output.manipulation;

import android.content.ContentValues;

import com.soybeany.bdlib.basic.database.core.DBReflectionUtils;
import com.soybeany.bdlib.basic.database.core.DBSwitchUtils;
import com.soybeany.bdlib.basic.database.interfaces.IConfig;
import com.soybeany.bdlib.basic.database.interfaces.IEntity;


/**
 * <br>Created by Soybeany on 2017/1/17.
 */
public class Insert {

    private static Insert mInstance = new Insert();

    private Insert() {

    }


    // //////////////////////////////////静态方法//////////////////////////////////

    /**
     * 获得单例
     */
    public static Insert getInstance() {
        return mInstance;
    }


    // //////////////////////////////////实例方法//////////////////////////////////

    /**
     * 执行
     */
    public long execute(IConfig dbConfig, Class<? extends IEntity> tableClass, ContentValues contentValues) {
        return DBSwitchUtils.getDBHelper(dbConfig).insert(DBReflectionUtils.getTableName(tableClass), contentValues);
    }
}
