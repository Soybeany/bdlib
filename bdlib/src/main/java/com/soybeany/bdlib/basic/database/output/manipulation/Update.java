package com.soybeany.bdlib.basic.database.output.manipulation;

import android.content.ContentValues;

import com.soybeany.bdlib.basic.database.core.DBReflectionUtils;
import com.soybeany.bdlib.basic.database.core.DBSwitchUtils;
import com.soybeany.bdlib.basic.database.interfaces.IConfig;
import com.soybeany.bdlib.basic.database.interfaces.IEntity;
import com.soybeany.bdlib.basic.database.interfaces.ISQLEntity;


/**
 * <br>Created by Soybeany on 2017/1/17.
 */
public class Update {

    private static Update mInstance = new Update();

    private Update() {

    }


    // //////////////////////////////////静态方法//////////////////////////////////

    /**
     * 获得单例
     */
    public static Update getInstance() {
        return mInstance;
    }


    // //////////////////////////////////实例方法//////////////////////////////////

    /**
     * 执行
     */
    public void execute(IConfig dbConfig, Class<? extends IEntity> tableClass, String where, ContentValues contentValues) {
        DBSwitchUtils.getDBHelper(dbConfig).update(DBReflectionUtils.getTableName(tableClass), where, contentValues);
    }

    /**
     * 使用对象执行
     */
    public void execute(IConfig dbConfig, Class<? extends IEntity> tableClass, ISQLEntity entity, ContentValues contentValues) {
        execute(dbConfig, tableClass, null != entity ? entity.getWhere() : null, contentValues);
    }
}
