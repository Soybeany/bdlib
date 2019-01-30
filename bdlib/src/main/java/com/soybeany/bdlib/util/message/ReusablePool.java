package com.soybeany.bdlib.util.message;

import java.util.LinkedList;

/**
 * 重用池
 * <br>Created by Soybeany on 2018/2/22.
 */
public abstract class ReusablePool<T> {

    private int mNum = 10; // 重用数目
    private LinkedList<T> mCache = new LinkedList<>(); // 缓存

    /**
     * 设置复用数目(默认为10)
     */
    public ReusablePool<T> num(int num) {
        mNum = num;
        return this;
    }

    /**
     * 取出对象
     */
    public T take() {
        T obj = mCache.pollFirst();
        return null != obj ? obj : getNew();
    }

    /**
     * 重用对象
     */
    public void recycle(T data) {
        if (mCache.size() < mNum) {
            mCache.addLast(data);
        } else {
            onRelease(data);
        }
    }

    /**
     * 释放全部对象
     */
    public void release() {
        for (T data : mCache) {
            onRelease(data);
        }
        mCache.clear();
    }

    /**
     * 创建新对象
     */
    protected abstract T getNew();

    /**
     * 释放对象时的回调
     */
    protected abstract void onRelease(T data);

}
