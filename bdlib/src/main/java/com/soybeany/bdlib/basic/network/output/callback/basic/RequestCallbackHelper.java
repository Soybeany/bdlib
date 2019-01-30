package com.soybeany.bdlib.basic.network.output.callback.basic;

/**
 * 请求回调辅助器
 * <br>Created by Soybeany on 2017/12/25.
 */
class RequestCallbackHelper {

    String destFileDir; // 目标文件夹
    String destFileName; // 目标文件名

    boolean isSmooth = true; // 标记是否顺利

    /**
     * 设置目标文件
     */
    RequestCallbackHelper destFile(String fileDir, String fileName) {
        destFileDir = fileDir;
        destFileName = fileName;
        return this;
    }

}
