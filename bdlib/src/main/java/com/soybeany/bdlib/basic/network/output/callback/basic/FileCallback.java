package com.soybeany.bdlib.basic.network.output.callback.basic;


import com.soybeany.bdlib.basic.network.interfaces.IRequest;
import com.soybeany.bdlib.basic.network.interfaces.IRequestCallback;
import com.soybeany.bdlib.basic.network.interfaces.IResponse;

import java.io.File;

/**
 * 文件回调（常用，最基本）
 * <br>Created by Soybeany on 2017/1/24.
 */
public abstract class FileCallback implements IRequestCallback<File> {

    private RequestCallbackHelper mHelper = new RequestCallbackHelper(); // 请求回调辅助器

    public FileCallback(String destFileDir, String destFileName) {
        mHelper.destFile(destFileDir, destFileName);
    }

    @Override
    public String getDestFileDir() {
        return mHelper.destFileDir;
    }

    @Override
    public String getDestFileName() {
        return mHelper.destFileName;
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
    public File onPreProcess(boolean hasResponse, File response, Object tag) {
        return response;
    }

    @Override
    public void onPostProcess(boolean isSmooth, File response, Object tag) {

    }

    @Override
    public boolean isSmooth() {
        return mHelper.isSmooth;
    }
}
