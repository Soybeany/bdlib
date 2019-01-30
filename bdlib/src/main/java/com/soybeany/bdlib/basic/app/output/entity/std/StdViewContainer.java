package com.soybeany.bdlib.basic.app.output.entity.std;

import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.soybeany.bdlib.R;
import com.soybeany.bdlib.basic.app.core.component.BaseToolbarManager;
import com.soybeany.bdlib.basic.app.core.component.BaseViewContainer;
import com.soybeany.bdlib.basic.app.core.component.ToolbarInfo;
import com.soybeany.bdlib.util.reflect.GenericUtils;

/**
 * 视图容器
 * <br>Created by Soybeany on 2017/9/3.
 */
public class StdViewContainer extends BaseViewContainer {

    private static final int CONTAINER_ID = R.id.bd_layout_container; // 容器id

    private AppCompatActivity mActivity; // 活动视图
    private BaseToolbarManager mToolbarManager; // 工具栏管理器
    private ViewGroup mContainerV; // 容器视图
    private ViewGroup mContentV; // 内容视图(用于存放开发者的视图)

    public StdViewContainer(AppCompatActivity activity) {
        mActivity = activity;
        mActivity.setContentView(setupContainerLayoutId());
        bindViews();
    }

    @Override
    public BaseToolbarManager getToolbarManager() {
        if (null == mToolbarManager) {
            mToolbarManager = getNewToolbarManager();
        }
        return mToolbarManager;
    }

    @Override
    public ToolbarInfo getNewToolbarInfo() {
        return new ToolbarInfo();
    }

    @Override
    public ViewGroup getContainerV() {
        return mContainerV;
    }

    @Override
    public ViewGroup getContentV() {
        return mContentV;
    }

    @Override
    public int getFragmentContainerId() {
        return CONTAINER_ID;
    }

    /**
     * 设置容器的布局id
     */
    protected int setupContainerLayoutId() {
        return R.layout.bd_app_activity_norm;
    }

    /**
     * 获得一个新的工具栏管理器
     */
    protected BaseToolbarManager getNewToolbarManager() {
        return new StdToolbarManager(mActivity);
    }

    /**
     * 绑定视图
     */
    protected void bindViews() {
        mContainerV = (ViewGroup) mActivity.findViewById(R.id.bd_layout_container);
        mContentV = (ViewGroup) mActivity.findViewById(R.id.bd_layout_content);
    }

    /**
     * 构建器
     */
    public static class Builder {

        private AppCompatActivity mActivity; // 活动页面
        private Class<? extends StdToolbarManager> mManager; // 工具栏管理器
        private Integer mLayoutId; // 布局id

        public Builder(AppCompatActivity activity) {
            mActivity = activity;
        }

        /**
         * 工具栏管理器(需要一个带{@link AppCompatActivity}的构造器)
         */
        public Builder toolbarManager(Class<? extends StdToolbarManager> manager) {
            mManager = manager;
            return this;
        }

        /**
         * 设置布局id
         */
        public Builder containerLayoutId(int layoutId) {
            mLayoutId = layoutId;
            return this;
        }

        /**
         * 构建标准的视图容器
         */
        public StdViewContainer build() {
            return new InnerContainer(mActivity);
        }

        /**
         * 内部的实现类
         */
        private class InnerContainer extends StdViewContainer {
            InnerContainer(AppCompatActivity activity) {
                super(activity);
            }

            @Override
            protected BaseToolbarManager getNewToolbarManager() {
                return null != mManager ? GenericUtils.getInstance(mManager, mActivity) : super.getNewToolbarManager();
            }

            @Override
            protected int setupContainerLayoutId() {
                return null != mLayoutId ? mLayoutId : super.setupContainerLayoutId();
            }
        }
    }

}
