package com.soybeany.bdlib.basic.app.core.component;

import android.support.v4.app.Fragment;

/**
 * 片段界面创建器，确保每次都能获得新的片段界面
 * <br>Created by Soybeany on 2017/3/18.
 */
public abstract class BaseFragmentCreator {

    /**
     * 获得新的片段界面
     */
    public abstract Fragment getNewFragment();

}
