package com.soybeany.bdlib.basic.widget.output.tabbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.basic.widget.util.DrawableUtils;
import com.soybeany.bdlib.util.context.BDContext;

/**
 * 标准的标签栏项
 * <br>Created by Soybeany on 2017/3/27.
 */
public class StdTabBarItem<Item extends StdTabBarItem> {

    private static int DEFAULT_ICON = R.drawable.bd_ic_home; // 默认图标资源
    private static String DEFAULT_DESC = "缺少描述"; // 默认描述

    private Context mContext; // 上下文
    private int mId; // 编号

    private String mDesc; // 描述

    private int mIconResId; // 图标资源
    private int mSelIconResId; // 被选中时的图标资源
    private int mNormColor = R.color.colorBlack; // 正常状态下的颜色
    private int mSelColor = R.color.colorBlue; // 选中状态下的颜色

    private boolean mIsTintMode = true; // 是否渲染模式
    private boolean mIsSelected; // 是否被选中状态

    private View mItemView; // 标签视图
    private ImageView mIconView; // 图标视图
    private TextView mDescView; // 描述视图

    public StdTabBarItem(Context context, int id) {
        mContext = context;
        mId = id;
    }

    /**
     * 获得此标签栏项的id
     */
    public int getId() {
        return mId;
    }

    /**
     * 设置图标资源id
     */
    public Item icon(int resId) {
        mIconResId = resId;
        return (Item) this;
    }

    /**
     * 设置描述
     */
    public Item desc(String desc) {
        mDesc = desc;
        return (Item) this;
    }

    /**
     * 禁用渲染模式
     *
     * @param selIconResId 被选中时的图标资源
     */
    public Item disableTintMode(int selIconResId) {
        mSelIconResId = selIconResId;
        mIsTintMode = false;
        return (Item) this;
    }

    /**
     * 设置布局的资源id
     */
    protected int setupLayoutResId() {
        return R.layout.bd_wdg_tb_item;
    }

    /**
     * 设置图标的id
     */
    protected int setupIconViewId() {
        return R.id.bd_tb_item_icon_iv;
    }

    /**
     * 设置描述的id
     */
    protected int setupDescViewId() {
        return R.id.bd_tb_item_desc_tv;
    }

    /**
     * 绑定视图
     */
    protected void bindViews(View rootView) {
        mIconView = (ImageView) rootView.findViewById(setupIconViewId());
        mDescView = (TextView) rootView.findViewById(setupDescViewId());
    }

    /**
     * 设置图标渲染的颜色
     *
     * @param normResId 正常状态下的颜色资源id
     * @param selResId  选中状态下的颜色资源id
     */
    StdTabBarItem tintColor(int normResId, int selResId) {
        mNormColor = normResId;
        mSelColor = selResId;
        return this;
    }

    /**
     * 设置是否选中
     */
    void setSelect(boolean flag) {
        mIsSelected = flag;
    }

    /**
     * 刷新标签
     */
    void refresh() {
        // 刷新标签内容
        int iconResId = !mIsTintMode && mIsSelected ? mSelIconResId : mIconResId;
        mIconView.setImageResource(0 != iconResId ? iconResId : DEFAULT_ICON);
        mDescView.setText(null != mDesc ? mDesc : DEFAULT_DESC);
        // 刷新标签颜色
        int color = mIsSelected ? mSelColor : mNormColor;
        mDescView.setTextColor(BDContext.getResources().getColor(color));
        if (mIsTintMode) {
            DrawableUtils.setColor(mIconView.getDrawable(), color);
        }
    }

    /**
     * 获得标签视图
     */
    View getItemView(ViewGroup parent) {
        if (null == mItemView) {
            mItemView = LayoutInflater.from(mContext).inflate(setupLayoutResId(), parent, false);
            bindViews(mItemView);
        }
        refresh();
        return mItemView;
    }

}
