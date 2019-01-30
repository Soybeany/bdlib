package com.soybeany.bdlib.basic.app.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.soybeany.bdlib.basic.app.annotation.ResultCodeType.RESULT_CANCELED;
import static com.soybeany.bdlib.basic.app.annotation.ResultCodeType.RESULT_OK;

/**
 * 结果码类型范围限定
 * <br>Created by Soybeany on 2017/3/15.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
@IntDef({RESULT_OK, RESULT_CANCELED})
public @interface ResultCodeType {

    /**
     * 正常状态
     */
    int RESULT_OK = -1;

    /**
     * 取消状态
     */
    int RESULT_CANCELED = 0;

}
