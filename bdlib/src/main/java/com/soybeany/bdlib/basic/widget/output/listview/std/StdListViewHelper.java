package com.soybeany.bdlib.basic.widget.output.listview.std;

import android.widget.ListView;

import com.soybeany.bdlib.basic.widget.output.listview.base.BaseListViewAdapter;
import com.soybeany.bdlib.basic.widget.output.listview.hint.HintListViewHelper;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewItem;
import com.soybeany.bdlib.basic.widget.output.listview.simple.SimpleItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.simple.SimpleListViewHelper;

/**
 * 标准的列表视图辅助器
 * <br>继承自：{@link SimpleListViewHelper}
 * <br>适配器：{@link BaseListViewAdapter}
 * <br>列表项工厂：{@link SimpleItemFactory}
 * <br>列表项数据：{@link StdListViewData}
 * <br>Created by Soybeany on 2017/3/17.
 */
public class StdListViewHelper extends HintListViewHelper<StdListViewData> {

    public StdListViewHelper(ListView listView) {
        this(listView, null, null);
    }

    public StdListViewHelper(ListView listView, IOnRefreshListener refreshListener, IOnLoadListener loadListener) {
        super(listView, new SimpleItemFactory<StdListViewData>(listView.getContext()) {
            @Override
            protected IListViewItem<StdListViewData> getItem(int type) {
                return new StdListViewItem();
            }
        }, refreshListener, loadListener);
    }
}
