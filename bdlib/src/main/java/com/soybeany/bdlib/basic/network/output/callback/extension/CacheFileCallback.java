package com.soybeany.bdlib.basic.network.output.callback.extension;

import com.soybeany.bdlib.util.file.FileUtils;

import java.io.File;

/**
 * 适用于文件缓存下载的回调，在成功下载文件后才替换掉指定文件
 * <br>Created by Soybeany on 2017/8/30.
 */
public abstract class CacheFileCallback extends TempFileCallback {

    private File mFormalFile; // 正式文件
    private File mTempFile; // 临时文件

    public CacheFileCallback(File file) {
        mFormalFile = file;
    }

    public CacheFileCallback(String destFileDir, String destFileName) {
        mFormalFile = new File(destFileDir, destFileName);
    }

    @Override
    public File onPreProcess(boolean hasResponse, File response, Object tag) {
        if (null == response) {
            return null;
        }
        mTempFile = response;
        if (response.isDirectory()) {
            FileUtils.copyFolder(mTempFile, mFormalFile);
        } else {
            FileUtils.copyFile(mTempFile, mFormalFile);
        }
        return mFormalFile;
    }

    @Override
    public void onPostProcess(boolean isSmooth, File response, Object tag) {
        deleteFile(mTempFile);
    }

}