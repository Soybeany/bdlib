package com.soybeany.bdlib.basic.network.interfaces;

/**
 * <br>Created by Soybeany on 2017/1/24.
 */
public interface IRequest {

    /**
     * 设置请求的标准提示语
     */
    IRequest stdHint(String stdHint);

    /**
     * 设置超时
     */
    IRequest timeout(long timeout);

    /**
     * 设置请求的标签，用于标识请求，常用于取消请求
     */
    IRequest requestTag(Object requestTag);

    /**
     * 设置自定义标签，作为回调时的参数返回
     */
    IRequest tag(Object tag);

    /**
     * 获得请求的标准提示语
     */
    String getStdHint();

    /**
     * 开始请求
     */
    void startRequest();

    /**
     * 取消请求
     */
    boolean cancelRequest();

}
