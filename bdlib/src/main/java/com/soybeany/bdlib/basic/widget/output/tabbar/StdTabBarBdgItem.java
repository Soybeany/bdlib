package com.soybeany.bdlib.basic.widget.output.tabbar;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.basic.widget.util.BadgeUtils;

/**
 * <br>Created by Soybeany on 2017/4/18.
 */
public class StdTabBarBdgItem extends StdTabBarItem<StdTabBarBdgItem> {

    private TextView mNumBadgeView; // 图标视图
    private ImageView mDotBadgeView; // 描述视图

    private int mMaxNum = BadgeUtils.NORM_MAX; // 直接显示的最大数目

    public StdTabBarBdgItem(Context context, int id) {
        super(context, id);
    }

    @Override
    protected int setupLayoutResId() {
        return R.layout.bd_wdg_tb_item_badge;
    }

    @Override
    protected int setupIconViewId() {
        return R.id.bd_tb_item_badge_icon_iv;
    }

    @Override
    protected int setupDescViewId() {
        return R.id.bd_tb_item_badge_desc_tv;
    }

    @Override
    protected void bindViews(View rootView) {
        super.bindViews(rootView);
        mNumBadgeView = (TextView) rootView.findViewById(R.id.bd_tb_item_badge_num_tv);
        mDotBadgeView = (ImageView) rootView.findViewById(R.id.bd_tb_item_badge_dot_iv);
    }

    /**
     * 设置直接显示的最大数目
     */
    public StdTabBarItem maxNum(int num) {
        mMaxNum = num;
        return this;
    }

    /**
     * 为数字角标设值
     */
    public void setNum(int num) {
        BadgeUtils.setBadgeNum(mNumBadgeView, num, BadgeUtils.NORM_MIN, mMaxNum, true, BadgeUtils.SMALL_WIDTH, BadgeUtils.SMALL_PADDING);
    }

    /**
     * 设置点角标
     */
    public void setDot(boolean visible) {
        mDotBadgeView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

}
