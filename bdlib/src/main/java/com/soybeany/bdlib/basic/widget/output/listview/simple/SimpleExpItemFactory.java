package com.soybeany.bdlib.basic.widget.output.listview.simple;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.basic.widget.output.listview.entity.ItemIndex;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IExpItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewItem;

/**
 * 简单的可展开的列表项工厂
 * <br>Created by Soybeany on 2017/3/17.
 */
public abstract class SimpleExpItemFactory<Group, Child> extends SimpleItemFactory<Group> implements IExpItemFactory<Group, Child> {

    public SimpleExpItemFactory(Context context) {
        super(context);
    }

    @Override
    public int getChildTypeCount() {
        return 1;
    }

    @Override
    public int getChildType(ItemIndex index, Child data) {
        return 0;
    }

    @Override
    public View getChildConvertView(int type, ViewGroup parent) {
        return inflateView(getChildItem(type), parent);
    }

    @Override
    public void setupChildConvertView(int position, boolean isLastChild, View convertView, Child data) {
        setupView(position, isLastChild, convertView, data);
    }

    /**
     * 获得子项
     *
     * @param type 所需子项的类型
     */
    protected abstract IListViewItem<Child> getChildItem(int type);
}
