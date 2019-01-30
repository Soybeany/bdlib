package com.soybeany.bdlib.util;

import com.soybeany.bdlib.util.log.LogUtils;
import com.soybeany.bdlib.util.system.EmergencyHandler;

/**
 * 基础模块（BD系列）
 * <br>Created by Soybeany on 2017/3/15.
 */
public class BDUtil {

    private static BDUtil mInstance = new BDUtil(); // 单例

    private BDUtil() {

    }


    // //////////////////////////////////静态方法区//////////////////////////////////

    /**
     * 获得单例
     */
    public static BDUtil getInstance() {
        return mInstance;
    }


    // //////////////////////////////////自定义配置//////////////////////////////////

    /**
     * 需要日志输出
     */
    public BDUtil needLog(boolean flag) {
        LogUtils.NEED_LOG = flag;
        return this;
    }

    /**
     * 需要崩溃日志
     */
    public BDUtil needCrashLog(String path, boolean addSdPrefix, EmergencyHandler.ICallback callback) {
        EmergencyHandler.init(path, addSdPrefix, callback);
        return this;
    }

}
