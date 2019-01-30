package com.soybeany.bdlib.basic.app.util;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.soybeany.bdlib.basic.app.core.component.ToolbarInfo;
import com.soybeany.bdlib.basic.app.core.interfaces.IFragmentResultListener;

/**
 * 全局信息传输工具类
 * <br>Created by Soybeany on 2017/3/14.
 */
public class GlobalInfoTransferUtils {

    /**
     * 保存请求码
     */
    public static void saveRequestCode(Fragment fragment, Integer requestCode) {
        DataTransferUtils.save(fragment, IFragmentResultListener.BD_REQUEST_CODE, requestCode);
    }

    /**
     * 读取请求码
     */
    public static Integer loadRequestCode(Fragment fragment) {
        return (Integer) DataTransferUtils.load(fragment, IFragmentResultListener.BD_REQUEST_CODE);
    }

    /**
     * 保存工具栏信息
     */
    public static void saveToolbarInfo(Fragment fragment, ToolbarInfo info) {
        DataTransferUtils.save(fragment, ToolbarInfo.KEY_TOOLBAR_INFO, info);
    }

    /**
     * 读取工具栏信息
     */
    public static ToolbarInfo loadToolbarInfo(Fragment fragment) {
        return (ToolbarInfo) DataTransferUtils.load(fragment, ToolbarInfo.KEY_TOOLBAR_INFO);
    }

    /**
     * 保存工具栏信息
     */
    public static void saveToolbarInfo(Intent intent, ToolbarInfo info) {
        DataTransferUtils.save(intent, ToolbarInfo.KEY_TOOLBAR_INFO, info);
    }

    /**
     * 读取工具栏信息
     */
    public static ToolbarInfo loadToolbarInfo(Intent intent) {
        return (ToolbarInfo) DataTransferUtils.load(intent, ToolbarInfo.KEY_TOOLBAR_INFO);
    }

}
