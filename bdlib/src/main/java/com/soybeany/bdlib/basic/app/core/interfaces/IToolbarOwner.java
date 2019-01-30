package com.soybeany.bdlib.basic.app.core.interfaces;

import android.support.v7.widget.Toolbar;

import com.soybeany.bdlib.basic.app.core.component.ToolbarInfo;

/**
 * 工具栏拥有者需遵循的协议
 * <br>Created by Ben on 2016/5/13.
 */
public interface IToolbarOwner {

    /**
     * 获得toolbar的信息
     */
    ToolbarInfo getToolbarInfo();

    /**
     * 获得toolbar
     */
    Toolbar getToolbar();

    /**
     * 用于对toolbar初始化
     *
     * @return true 表示执行完此方法后立刻刷新toolbar(调用{@link #updateToolbar()})
     */
    boolean initToolbar(ToolbarInfo info);

    /**
     * 更新toolbar
     */
    void updateToolbar();

}
