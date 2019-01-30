package com.soybeany.bdlib.basic.app.core.interfaces;

import android.support.v4.app.FragmentActivity;

/**
 * 表示此页面具有标识符功能的接口
 * <br>Created by Soybeany on 2017/3/14.
 */
public interface IIdentifier {

    /**
     * 获取当前activity
     */
    FragmentActivity getActivity();

    /**
     * 获取当前类的标识
     */
    String getClassTag();

}
