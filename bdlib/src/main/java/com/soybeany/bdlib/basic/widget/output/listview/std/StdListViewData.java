package com.soybeany.bdlib.basic.widget.output.listview.std;

import com.soybeany.bdlib.basic.widget.output.listview.entity.SelectableItem;

/**
 * 标准的列表视图数据
 * <br>Created by Ben on 2016/5/27.
 */
public class StdListViewData extends SelectableItem<StdListViewData> {

    /**
     * 图标
     */
    public int icon;

    /**
     * 名字
     */
    public String name;

    /**
     * 数目标记
     */
    public int badge;

    /**
     * 箭头颜色资源id
     */
    public int arrowColorResId = -1;

    /**
     * 需要显示右箭头
     */
    public boolean needArrow = true;

    /**
     * 链式设置图片资源
     */
    public StdListViewData icon(int icon) {
        this.icon = icon;
        return this;
    }

    /**
     * 链式设置名字
     */
    public StdListViewData name(String name) {
        this.name = name;
        return this;
    }

    /**
     * 链式设置数目标记
     */
    public StdListViewData badge(int badge) {
        this.badge = badge;
        return this;
    }

    /**
     * 链式设置箭头颜色资源id
     */
    public StdListViewData arrowColor(int resId) {
        this.arrowColorResId = resId;
        return this;
    }

    /**
     * 链式设置是否显示右箭头
     */
    public StdListViewData needArrow(boolean show) {
        this.needArrow = show;
        return this;
    }

}
