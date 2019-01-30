package com.soybeany.bdlib.basic.widget.output.listview.simple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewItem;

/**
 * 简单的列表项工厂
 * <br>Created by Soybeany on 2017/3/16.
 */
public abstract class SimpleItemFactory<Data> implements IItemFactory<Data> {

    private Context mContext;

    public SimpleItemFactory(Context context) {
        mContext = context;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public int getItemTypeCount() {
        return 1;
    }

    @Override
    public int getItemType(int position, Data data) {
        return 0;
    }

    @Override
    public View getConvertView(int type, ViewGroup parent) {
        return inflateView(getItem(type), parent);
    }

    @Override
    public void setupConvertView(int position, boolean isExpanded, View convertView, Data data) {
        setupView(position, isExpanded, convertView, data);
    }

    /**
     * 渲染视图，包括将IListViewItem赋值给convertView的Tag
     */
    protected View inflateView(IListViewItem item, ViewGroup parent) {
        View convertView = LayoutInflater.from(mContext).inflate(item.setupItemResId(), parent, false);
        item.bindViews(convertView);
        item.initViews();
        convertView.setTag(item);
        return convertView;
    }

    /**
     * 设置视图，包括将IListViewItem从convertView的Tag中读出
     */
    @SuppressWarnings("unchecked")
    protected void setupView(int position, boolean isExpanded, View convertView, Object data) {
        IListViewItem item = (IListViewItem) convertView.getTag();
        item.setupView(position, isExpanded, data);
    }

    /**
     * 获得列表项
     *
     * @param type 所需列表项的类型
     */
    protected abstract IListViewItem<Data> getItem(int type);

}
