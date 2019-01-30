package com.soybeany.bdlib.basic.widget.output.listview.std;

import android.widget.ExpandableListView;

import com.soybeany.bdlib.basic.widget.output.listview.hint.HintExpListViewHelper;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewItem;
import com.soybeany.bdlib.basic.widget.output.listview.simple.SimpleExpItemFactory;

/**
 * 标准的可展开列表视图辅助器
 * <br>Created by Soybeany on 2017/3/19.
 */
public class StdExpListViewHelper extends HintExpListViewHelper<StdListViewData, StdListViewData> {

    public StdExpListViewHelper(ExpandableListView listView, boolean isGroupClickable) {
        this(listView, isGroupClickable, null, null);
    }

    public StdExpListViewHelper(ExpandableListView listView, final boolean isGroupClickable, IOnRefreshListener refreshListener, IOnLoadListener loadListener) {
        super(listView, new SimpleExpItemFactory<StdListViewData, StdListViewData>(listView.getContext()) {
            @Override
            protected IListViewItem<StdListViewData> getChildItem(int type) {
                return new StdListViewItem();
            }

            @Override
            protected IListViewItem<StdListViewData> getItem(int type) {
                return new StdListViewGroupItem().clickable(isGroupClickable);
            }
        }, refreshListener, loadListener);
    }
}
