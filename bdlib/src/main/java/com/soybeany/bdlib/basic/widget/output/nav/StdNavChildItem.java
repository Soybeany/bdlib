package com.soybeany.bdlib.basic.widget.output.nav;

import android.view.View;
import android.widget.TextView;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.basic.widget.output.listview.interfaces.IListViewItem;
import com.soybeany.bdlib.basic.widget.util.BadgeUtils;
import com.soybeany.bdlib.basic.widget.util.DrawableUtils;

/**
 * 标准的导航栏子项栏
 * <br>Created by Soybeany on 2017/3/19.
 */
public class StdNavChildItem implements IListViewItem<StdNavItemData> {

    private View mConvertView; // 转换视图
    private TextView mNameV; // 名字视图
    private TextView mBadgeV; // 标记视图

    @Override
    public int setupItemResId() {
        return R.layout.bd_wdg_lv_item_nav_child;
    }

    @Override
    public void bindViews(View convertView) {
        mConvertView = convertView;
        mNameV = (TextView) mConvertView.findViewById(R.id.bd_lv_item_nav_child_name_tv);
        mBadgeV = (TextView) mConvertView.findViewById(R.id.bd_lv_item_nav_child_badge_tv);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void setupView(int position, boolean flag, StdNavItemData data) {
        // 设置图标
        mNameV.setCompoundDrawables(DrawableUtils.start(data.icon).resize(DrawableUtils.ICON_SIZE_DEFAULT).build(), null, null, null);
        // 设置名字
        mNameV.setText(data.name);
        // 设置标记
        BadgeUtils.setBadgeNum(mBadgeV, data.badge, 0, 999, true);
        // 设置是否被选中
        mConvertView.setBackgroundResource(data.needSelected ? data.selBackgroundResId : data.backgroundResId);
    }

}
