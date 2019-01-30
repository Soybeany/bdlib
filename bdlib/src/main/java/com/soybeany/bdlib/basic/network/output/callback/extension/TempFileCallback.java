package com.soybeany.bdlib.basic.network.output.callback.extension;

import com.soybeany.bdlib.basic.network.output.callback.basic.FileCallback;
import com.soybeany.bdlib.util.file.FileUtils;
import com.soybeany.bdlib.util.log.LogUtils;

import java.io.File;

/**
 * 适用于临时使用文件数据的回调，在回调结束后将自动删除该文件
 * <br>Created by Soybeany on 2017/8/30.
 */
public abstract class TempFileCallback extends FileCallback {

    public TempFileCallback() {
        super(FileUtils.getTempFileDir(), FileUtils.getTempFileName());
    }

    @Override
    public void onPostProcess(boolean isSmooth, File response, Object tag) {
        deleteFile(response);
    }

    /**
     * 删除文件
     */
    protected void deleteFile(File file) {
        if (file != null) {
            LogUtils.i(getClass().getSimpleName(), "删除临时文件" + (FileUtils.deleteFile(file) ? "成功" : "失败"));
        }
    }

}