package com.soybeany.bdlib.basic.widget.output.nav;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import com.soybeany.bdlib.basic.widget.output.listview.entity.GroupItemData;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IExpItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.simple.SimpleExpListViewHelper;

import java.util.List;

/**
 * 基础的导航辅助器(使用可展开列表视图)
 * <br>Created by Soybeany on 2017/9/4.
 */
public abstract class BaseExpListViewNavHelper<Group, Child> extends BaseNavHelper {

    private SimpleExpListViewHelper<Group, Child> mListViewHelper; // 列表辅助器

    public BaseExpListViewNavHelper(Activity activity, Toolbar toolbar, DrawerLayout drawerLayout, int expListViewId, IExpItemFactory<Group, Child> factory) {
        super(activity, toolbar, drawerLayout, expListViewId);
        mListViewHelper = new SimpleExpListViewHelper<>((ExpandableListView) getBodyView(), factory);
    }

    /**
     * 获得相应的列表视图辅助器
     */
    public SimpleExpListViewHelper<Group, Child> getListViewHelper() {
        return mListViewHelper;
    }

    /**
     * 设置数据
     */
    public void setData(List<GroupItemData<Group, Child>> data) {
        mListViewHelper.clearData();
        for (GroupItemData<Group, Child> itemData : data) {
            mListViewHelper.addData(itemData);
        }
    }

}
