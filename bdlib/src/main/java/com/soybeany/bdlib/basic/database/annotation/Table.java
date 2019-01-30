package com.soybeany.bdlib.basic.database.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 数据表
 * <br>Created by Soybeany on 2017/1/19.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * 默认ID名
     */
    String DEFAULT_ID_NAME = "id";

    /**
     * 表名
     */
    String name();

}
