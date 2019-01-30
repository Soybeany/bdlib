package com.soybeany.bdlib.basic.widget.output.nav;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;

import com.soybeany.bdlib.basic.widget.output.listview.entity.ItemIndex;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewItem;
import com.soybeany.bdlib.basic.widget.output.listview.simple.SimpleExpItemFactory;
import com.soybeany.bdlib.basic.widget.output.listview.simple.SimpleExpListViewHelper;

/**
 * <br>Created by Soybeany on 2017/9/4.
 */
public class ExpListViewNavHelper extends BaseExpListViewNavHelper<StdNavItemData, StdNavItemData> {

    private SimpleExpListViewHelper<StdNavItemData, StdNavItemData> mLVHelper; // 列表项辅助器
    private ItemIndex mSelectedIndex = new ItemIndex(-1, -1); // 最后点击的item
    private IOnChildClickListener mListener; // 子项点击监听器

    public ExpListViewNavHelper(Activity activity, Toolbar toolbar, DrawerLayout drawerLayout, int expListViewId) {
        super(activity, toolbar, drawerLayout, expListViewId, new SimpleExpItemFactory<StdNavItemData, StdNavItemData>(activity) {
            @Override
            protected IListViewItem<StdNavItemData> getChildItem(int type) {
                return new StdNavChildItem();
            }

            @Override
            protected IListViewItem<StdNavItemData> getItem(int type) {
                return new StdNavGroupItem();
            }
        });
        mLVHelper = getListViewHelper();
        // 设置子项的点击监听
        mLVHelper.getListView().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return null != mListener && mListener.onClick(parent, v, groupPosition, childPosition, id);
            }
        });
    }

    /**
     * 获得所选择item的位置
     */
    public ItemIndex getSelectedItem() {
        return mSelectedIndex;
    }

    /**
     * 设置所选择item的位置
     */
    public void setSelectedItem(ItemIndex index) {
        mSelectedIndex = index;
        // 重置选中标识
        for (int i = 0; i < mLVHelper.getGroupDataList().size(); i++) {
            for (StdNavItemData data : mLVHelper.getChildDataList(i)) {
                data.needSelected = false;
            }
        }
        // 设置指定位置的标识
        mLVHelper.getChildData(mSelectedIndex).needSelected = true;
        // 刷新表视图
        mLVHelper.refreshListView();
    }

    /**
     * 设置子项点击监听器
     */
    public void setOnItemClickListener(IOnChildClickListener listener) {
        mListener = listener;
    }

    /**
     * 子项被点击的监听器
     */
    public interface IOnChildClickListener {

        /**
         * 被点击时的回调
         *
         * @param v 被点击的视图
         * @return true为点击事件已被处理
         */
        boolean onClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id);

    }

}
