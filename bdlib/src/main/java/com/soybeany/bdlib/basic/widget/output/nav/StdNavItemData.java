package com.soybeany.bdlib.basic.widget.output.nav;

import com.soybeany.bdlib.basic.widget.output.listview.entity.SelectableItem;

/**
 * 标准的导航栏数据
 * <br>Created by Soybeany on 2017/3/19.
 */
public class StdNavItemData<Item extends StdNavItemData> extends SelectableItem<Item> {

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
    public int badge = -1;

    /**
     * 链式设置图片资源
     */
    public Item icon(int icon) {
        this.icon = icon;
        return (Item) this;
    }

    /**
     * 链式设置名字
     */
    public Item name(String name) {
        this.name = name;
        return (Item) this;
    }

    /**
     * 链式设置数目标记
     */
    public Item badge(int badge) {
        this.badge = badge;
        return (Item) this;
    }

}
