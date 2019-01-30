package com.soybeany.bdlib.basic.widget.output.listview.interfaces;

import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.basic.widget.output.listview.entity.ItemIndex;

/**
 * 可展开列表项工厂
 * <br>Created by Soybeany on 2017/3/16.
 */
public interface IExpItemFactory<Group, Child> extends IItemFactory<Group> {

    /**
     * 获得子项类型的数目
     */
    int getChildTypeCount();

    /**
     * 获得子项的类型
     */
    int getChildType(ItemIndex index, Child data);

    /**
     * 获得子项转换视图
     */
    View getChildConvertView(int type, ViewGroup parent);

    /**
     * 设置子项转换视图
     */
    void setupChildConvertView(int position, boolean isLastChild, View convertView, Child data);
}
