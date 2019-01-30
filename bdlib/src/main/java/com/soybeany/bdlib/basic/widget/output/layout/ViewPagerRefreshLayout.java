package com.soybeany.bdlib.basic.widget.output.layout;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.soybeany.bdlib.util.context.BDContext;

/**
 * 适用于ViewPager的刷新布局(解决手势冲突)
 * <br>Created by Soybeany on 2017/9/11.
 */
public class ViewPagerRefreshLayout extends SwipeRefreshLayout {

    private float startY;
    private float startX;

    private boolean mIsVpDragging; // 记录viewPager是否拖拽的标记
    private final int mTouchSlop;

    public ViewPagerRefreshLayout(Context context) {
        this(context, null);
    }

    public ViewPagerRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setColorSchemeColors(BDContext.getColorPrimary(context));
        fixRefreshBug();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
                mIsVpDragging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if (mIsVpDragging) {
                    return false;
                }

                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    mIsVpDragging = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                mIsVpDragging = false;
                break;
        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 以临时方案解决官方bug todo 官方修正后删除
     */
    private void fixRefreshBug() {
        setRefreshing(true);
        setRefreshing(false);
        setEnabled(false);
        setEnabled(true);
    }
}
