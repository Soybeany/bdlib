package com.soybeany.bdlib.basic.database.output.impl;

import android.content.ContentValues;

import com.soybeany.bdlib.basic.database.annotation.Column;
import com.soybeany.bdlib.basic.database.annotation.Table;
import com.soybeany.bdlib.basic.database.core.DBReflectionUtils;
import com.soybeany.bdlib.basic.database.core.DBSwitchUtils;
import com.soybeany.bdlib.basic.database.interfaces.IConfig;
import com.soybeany.bdlib.basic.database.interfaces.IEntity;
import com.soybeany.bdlib.basic.database.interfaces.IManipulation;
import com.soybeany.bdlib.basic.database.interfaces.ISQLEntity;

import java.util.List;


/**
 * 实体基类【为了能成功反射，子类一定要提供一个公共的无参构造方法】（默认实现）
 * <br>Created by Soybeany on 2017/1/12.
 */
public abstract class Entity implements IEntity {

    private IManipulation mDBManipulation; // 数据库辅助类
    private String mTableName = DBReflectionUtils.getTableName(getClass()); // 表名

    @Column(name = Table.DEFAULT_ID_NAME)
    private Long mId = null; // 表的自增主键字段

    protected Entity() {
        IConfig dbConfig = setupDefaultDBConfig();
        if (null != dbConfig) {
            dbConfig(dbConfig);
        }
    }

    @Override
    public Long getId() {
        return mId;
    }

    @Override
    public long save() {
        ContentValues values = DBReflectionUtils.getContentValues(this);
        if (mId == null) {
            // 没有数据就插入
            mId = mDBManipulation.insert(mTableName, values);
        } else {
            // 有数据就更新
            mDBManipulation.update(mTableName, mId, values);
        }
        return mId;
    }

    @Override
    public void delete() {
        mDBManipulation.delete(mTableName, mId);
    }

    @Override
    public Entity dbConfig(IConfig dbConfig) {
        mDBManipulation = DBSwitchUtils.getDBHelper(dbConfig);
        return this;
    }

    /**
     * 按条件从数据库中查出数据，并将其id绑定到此对象
     *
     * @param entity 查询条件实体
     * @return 是否绑定成功(查找结果条数等于1为成功)
     */
    protected boolean bindId(ISQLEntity entity) {
        List<? extends Entity> query = mDBManipulation.query(entity.isDistinct(), getClass(), entity.getColumns(), entity.getWhere(), entity.getGroupBy(), entity.getHaving(), entity.getOrderBy(), entity.getLimit());
        if (1 == query.size()) {
            mId = query.get(0).getId();
            return true;
        }
        return false;
    }

    /**
     * 设置默认数据库配置
     */
    protected IConfig setupDefaultDBConfig() {
        return null;
    }
}
