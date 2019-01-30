package com.soybeany.bdlib.basic.app.core.component;

import android.support.v4.app.Fragment;

/**
 * <br>Created by Soybeany on 2017/3/14.
 */
public abstract class BaseFragmentManager {

    /**
     * 获取栈顶的fragment
     */
    public abstract Fragment getTopFragment();

    /**
     * 推入fragment
     *
     * @param requestCode 传入null时则不带回调
     */
    public abstract void pushFragment(Fragment fragment, ToolbarInfo info, Integer requestCode);

    /**
     * 弹出fragment（直接）
     */
    public abstract boolean popFragment();

    /**
     * 贴上fragment(返回键不会将其弹出)
     */
    public abstract void attachFragment(ToolbarInfo info, Fragment fragment);


    /**
     * 操作的回调
     */
    public interface IOptionCallback {
        /**
         * 推入时的回调
         *
         * @param isEmpty fragment栈是否为空
         */
        void onPush(boolean isEmpty);

        /**
         * 弹出时的回调
         *
         * @param isEmpty fragment栈是否为空
         */
        void onPop(boolean isEmpty);

        /**
         * 推入或弹出后的回调
         *
         * @param needToUpdate 是否需要更新，因为每个页面都有自己的更新逻辑，所以需要避免重复更新
         */
        void afterPushOrPop(boolean needToUpdate);
    }

}
