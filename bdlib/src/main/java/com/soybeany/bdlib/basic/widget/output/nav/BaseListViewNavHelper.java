package com.soybeany.bdlib.basic.widget.output.nav;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.simple.SimpleListViewHelper;

import java.util.Arrays;

/**
 * 基础的导航辅助器(使用列表视图)
 * <br>Created by Soybeany on 2017/9/4.
 */
public class BaseListViewNavHelper<Data> extends BaseNavHelper {

    private SimpleListViewHelper<Data> mListViewHelper; // 列表辅助器

    public BaseListViewNavHelper(Activity activity, Toolbar toolbar, DrawerLayout drawerLayout, int listViewId, IItemFactory<Data> factory) {
        super(activity, toolbar, drawerLayout, listViewId);
        mListViewHelper = new SimpleListViewHelper<>((ListView) getBodyView(), factory);
    }

    /**
     * 获得相应的列表视图辅助器
     */
    public SimpleListViewHelper<Data> getListViewHelper() {
        return mListViewHelper;
    }

    /**
     * 设置数据
     */
    public void setData(Data[] data) {
        mListViewHelper.setData(Arrays.asList(data));
    }

}
