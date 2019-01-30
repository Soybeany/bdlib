package com.soybeany.bdlib.basic.app.core.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.soybeany.bdlib.util.reflect.ReflectUtils;

/**
 * 视图持有器，MVP模式中的V层，持有布局文件中的控件，便于代码中的动态操作
 * <br>Created by Soybeany on 2017/3/13.
 */
public abstract class BaseViewHolder {

    private Context mContext; // 上下文

    /**
     * 框架内调用，一般不需在外面再调用
     */
    public void setContext(Context context) {
        mContext = context;
    }

    /**
     * 获得布局渲染器
     */
    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    /**
     * 绑定视图，将代码中的控件绑定到布局中的控件，或者代码生成的控件
     */
    public abstract void bindViews(View rootView);

    /**
     * 对控件进行一些初始化操作，例如设置监听器等
     */
    public abstract void initViews();

    /**
     * 设置监听器
     */
    public static void setupListeners(View.OnClickListener listener, View... views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

    /**
     * 设置标记(搭配{@link TagListener}使用)
     */
    public static void setupTag(Object tag, View... views) {
        for (View view : views) {
            view.setTag(tag);
        }
    }

    /**
     * 标记监听器
     */
    public static abstract class TagListener<T> implements View.OnClickListener {
        @SuppressWarnings("unchecked")
        @Override
        public void onClick(View v) {
            onClick((T) v.getTag());
        }

        /**
         * 点击监听器
         */
        protected abstract void onClick(T tag);
    }

    /**
     * 点击时的反射监听器(用于与P层交互)
     */
    protected static class OnClickReflectListener implements View.OnClickListener {
        private Object mObj; // 执行对象(P层)
        private String mName; // 方法名
        private Object[] mArgs; // 参数

        public OnClickReflectListener(Object obj, String name, Object... args) {
            mObj = obj;
            mName = name;
            mArgs = args;
        }

        @Override
        public void onClick(View v) {
            ReflectUtils.invoke(mObj, mName, mArgs);
        }
    }

}
