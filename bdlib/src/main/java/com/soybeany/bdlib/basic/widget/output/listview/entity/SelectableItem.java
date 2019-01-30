package com.soybeany.bdlib.basic.widget.output.listview.entity;

import com.soybeany.bdlib.R;

/**
 * 可选择的列表项
 * <br>Created by Soybeany on 2017/3/17.
 */
public class SelectableItem<Item extends SelectableItem> {

    /**
     * 背景资源id
     */
    public int backgroundResId;

    /**
     * 选中时的背景资源id
     */
    public int selBackgroundResId = R.color.colorTransGray9;

    /**
     * 是否需要被选中
     */
    public boolean needSelected;

    /**
     * 链式设置背景资源id
     */
    @SuppressWarnings("unchecked")
    public Item background(int resId) {
        this.backgroundResId = resId;
        return (Item) this;
    }

    /**
     * 链式设置选中时的背景资源id
     */
    @SuppressWarnings("unchecked")
    public Item selBackground(int resId) {
        this.selBackgroundResId = resId;
        return (Item) this;
    }

    /**
     * 链式设置是否被选中
     */
    @SuppressWarnings("unchecked")
    public Item needSelected(boolean needSelected) {
        this.needSelected = needSelected;
        return (Item) this;
    }

}
