package com.soybeany.bdlib.basic.widget.interfaces;

/**
 * 内容展示者，能隐藏或显示其内容
 * <br>Created by Soybeany on 2017/3/16.
 */
public interface IContentPresenter {

    /**
     * 显示内容
     */
    void showContent();

    /**
     * 隐藏内容
     *
     * @param hint           隐藏内容时的提示语
     * @param needRefreshBtn 是否需要刷新按钮
     */
    void hideContent(String hint, boolean needRefreshBtn);

    /**
     * 内容状态监听者
     */
    interface IContentStateListener {

        /**
         * 内容显示时的回调
         */
        void onShow();

        /**
         * 内容隐藏时的回调
         */
        void onHide();

    }

}
