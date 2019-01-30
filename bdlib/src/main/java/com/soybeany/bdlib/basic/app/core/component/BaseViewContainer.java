package com.soybeany.bdlib.basic.app.core.component;

import android.view.ViewGroup;

/**
 * 视图容器
 * <br>Created by Soybeany on 2017/9/3.
 */
public abstract class BaseViewContainer<Info extends ToolbarInfo> {

    /**
     * 获得新的工具栏管理器
     */
    public abstract BaseToolbarManager getToolbarManager();

    /**
     * 获得新的工具栏信息
     */
    public abstract Info getNewToolbarInfo();

    /**
     * 获得容器视图
     */
    public abstract ViewGroup getContainerV();

    /**
     * 获得内容视图(存放开发者的页面)
     */
    public abstract ViewGroup getContentV();

    /**
     * 获得片段容器的id
     */
    public abstract int getFragmentContainerId();

}
