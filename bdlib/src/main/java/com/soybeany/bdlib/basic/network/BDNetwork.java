package com.soybeany.bdlib.basic.network;

import com.soybeany.bdlib.basic.network.interfaces.IRequestCallback;
import com.soybeany.bdlib.basic.network.output.request.BDRequest;
import com.soybeany.bdlib.util.text.StdHintUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 网络请求模块（BD系列）
 * (工厂方法模式)
 * <br>Created by Soybeany on 2017/1/24.
 */
public class BDNetwork {

    private static BDNetwork mInstance = new BDNetwork(); // 单例

    private BDNetwork() {

    }


    // //////////////////////////////////静态方法区//////////////////////////////////

    /**
     * 获得单例
     */
    public static BDNetwork getInstance() {
        return mInstance;
    }

    /**
     * 释放占用的资源（一般在Application的onTerminate中调用）
     */
    public static void release() {
        cancelAll(BDRequest.DEFAULT_REQUEST_TAG);
    }

    /**
     * 根据tag值取消全部请求
     */
    public static void cancelAll(Object tag) {
        BDRequest.cancelAll(tag);
    }


    // //////////////////////////////////请求区//////////////////////////////////

    /**
     * get请求
     */
    public static BDRequest get(String url, Map<String, String> requestParam, IRequestCallback callback) {
        return new BDRequest(BDRequest.TYPE_GET, url, requestParam, callback).stdHint(StdHintUtils.STD_GET_HINT);
    }

    /**
     * post请求
     */
    public static BDRequest post(String url, Map<String, String> requestParam, IRequestCallback callback) {
        return new BDRequest(BDRequest.TYPE_POST, url, requestParam, callback).stdHint(StdHintUtils.STD_POST_HINT);
    }

    /**
     * 文件下载
     */
    public static BDRequest download(String url, Map<String, String> requestParam, IRequestCallback callback) {
        return new BDRequest(BDRequest.TYPE_DOWNLOAD, url, requestParam, callback).stdHint(StdHintUtils.STD_DOWNLOAD_HINT);
    }

    /**
     * 文件上传
     */
    public static BDRequest upload(String url, File destFile, IRequestCallback callback) {
        return new BDRequest(BDRequest.TYPE_UPLOAD, url, destFile, callback).stdHint(StdHintUtils.STD_UPLOAD_HINT);
    }

    /**
     * 文件上传
     */
    public static BDRequest upload(String url, Map<String, String> requestParam, List<BDRequest.UploadFile> uploadFiles, IRequestCallback callback) {
        return new BDRequest(BDRequest.TYPE_POST, url, requestParam, uploadFiles, callback).stdHint(StdHintUtils.STD_UPLOAD_HINT);
    }


    // //////////////////////////////////自定义配置//////////////////////////////////

    /**
     * 默认超时
     */
    public void defaultTimeOut(long timeOut) {
        BDRequest.DEFAULT_TIME_OUT = timeOut;
    }

}
