package com.soybeany.bdlib.basic.database.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 数据列
 * <br>Created by Soybeany on 2017/1/19.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    String name() default "";

    int length() default -1;

}
