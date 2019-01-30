package com.soybeany.bdlib.basic.network.output.callback.basic;


import com.soybeany.bdlib.basic.network.interfaces.IRequest;
import com.soybeany.bdlib.basic.network.interfaces.IRequestCallback;
import com.soybeany.bdlib.basic.network.interfaces.IResponse;

/**
 * 字符串回调（常用，最基本）
 * <br>Created by Soybeany on 2017/1/24.
 */
public abstract class StringCallback implements IRequestCallback<String> {

    private RequestCallbackHelper mHelper = new RequestCallbackHelper(); // 请求回调辅助器

    @Override
    public String getDestFileDir() {
        return null;
    }

    @Override
    public String getDestFileName() {
        return null;
    }

    @Override
    public void onStart(IRequest request, Object tag) {

    }

    @Override
    public void onReceive(IResponse response, Object tag) {

    }

    @Override
    public void inProgress(float progress, long total, Object tag) {

    }

    @Override
    public void onFinish(Object tag) {

    }

    @Override
    public void onFinal(Object tag) {

    }

    @Override
    public void onPreStart(Object tag) {

    }

    @Override
    public String onPreProcess(boolean hasResponse, String response, Object tag) {
        return response;
    }

    @Override
    public void onPostProcess(boolean isSmooth, String response, Object tag) {

    }

    @Override
    public boolean isSmooth() {
        return mHelper.isSmooth;
    }
}
