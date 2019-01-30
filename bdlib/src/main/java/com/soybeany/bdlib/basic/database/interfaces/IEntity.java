package com.soybeany.bdlib.basic.database.interfaces;

/**
 * 数据库实体接口
 * <br>Created by Soybeany on 2017/1/16.
 */
public interface IEntity {

    /**
     * 默认主键编号的字段
     */
    String DEFAULT_ID_FIELD = "mId";

    /**
     * 获得此条记录的主键编号
     */
    Long getId();

    /**
     * 保存本条数据（增加/修改）
     */
    long save();

    /**
     * 删除本条数据
     */
    void delete();

    /**
     * 设置数据库配置
     */
    IEntity dbConfig(IConfig dbConfig);

}
