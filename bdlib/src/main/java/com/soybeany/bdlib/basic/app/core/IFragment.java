package com.soybeany.bdlib.basic.app.core;

import android.os.Bundle;

import com.soybeany.bdlib.basic.app.annotation.BackType;
import com.soybeany.bdlib.basic.app.annotation.ResultCodeType;
import com.soybeany.bdlib.basic.app.core.interfaces.IFragmentResultListener;

/**
 * 标准Fragment需遵循的协议
 * <br>Created by Ben on 2016/5/13.
 */
public interface IFragment extends IFragmentResultListener {

    /**
     * 获得数据集
     */
    Bundle getDataBundle();

    /**
     * 获得fragment的结果码
     */
    int getResultCode();

    /**
     * 设置fragment的结果码
     */
    void setResultCode(@ResultCodeType int resultCode);

    /**
     * 弹出当前fragment（间接），若为最后一个则结束当前activity(可以在工程基类重写)
     */
    void popThisFragment(@BackType int backType);

}
