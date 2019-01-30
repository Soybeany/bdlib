package com.soybeany.bdlib.basic.widget.output.listview.std;

import android.support.v7.widget.RecyclerView;

import com.soybeany.bdlib.basic.widget.output.listview.hint.HintRecyclerViewHelper;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewItem;
import com.soybeany.bdlib.basic.widget.output.listview.simple.SimpleItemFactory;

/**
 * <br>Created by Soybeany on 2017/3/17.
 */
public class StdRecyclerViewHelper extends HintRecyclerViewHelper<StdListViewData> {

    public StdRecyclerViewHelper(RecyclerView listView) {
        this(listView, null, null);
    }

    public StdRecyclerViewHelper(RecyclerView listView, IOnRefreshListener refreshListener, IOnLoadListener loadListener) {
        super(listView, new SimpleItemFactory<StdListViewData>(listView.getContext()) {
            @Override
            protected IListViewItem<StdListViewData> getItem(int type) {
                return new StdListViewItem().needDefBg();
            }
        }, refreshListener, loadListener);
    }
}
