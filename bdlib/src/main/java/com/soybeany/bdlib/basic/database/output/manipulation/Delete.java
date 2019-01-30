package com.soybeany.bdlib.basic.database.output.manipulation;


import com.soybeany.bdlib.basic.database.core.DBReflectionUtils;
import com.soybeany.bdlib.basic.database.core.DBSwitchUtils;
import com.soybeany.bdlib.basic.database.interfaces.IConfig;
import com.soybeany.bdlib.basic.database.interfaces.IEntity;
import com.soybeany.bdlib.basic.database.interfaces.ISQLEntity;

/**
 * <br>Created by Soybeany on 2017/1/17.
 */
public class Delete {

    private static Delete mInstance = new Delete();

    private Delete() {

    }


    // //////////////////////////////////静态方法//////////////////////////////////

    /**
     * 获得单例
     */
    public static Delete getInstance() {
        return mInstance;
    }


    // //////////////////////////////////实例方法//////////////////////////////////

    /**
     * 执行
     */
    public void execute(IConfig dbConfig, Class<? extends IEntity> tableClass, String where) {
        DBSwitchUtils.getDBHelper(dbConfig).delete(DBReflectionUtils.getTableName(tableClass), where);
    }

    /**
     * 执行
     */
    public void execute(IConfig dbConfig, Class<? extends IEntity> tableClass, ISQLEntity entity) {
        execute(dbConfig, tableClass, null != entity ? entity.getWhere() : null);
    }
}
