package com.soybeany.bdlib.basic.widget;

/**
 * 视图模块（BD系列）
 * <br>概述：
 * <br>1.对官方控件增加一层封装，故效率与直接使用官方控件无异
 * <br>2.使用的api更适用于日常快速开发（简洁、稳定）
 * <br>Created by Soybeany on 2017/3/12.
 */
public class BDWidget {

    private static BDWidget mInstance = new BDWidget(); // 单例

    private BDWidget() {

    }


    // //////////////////////////////////静态方法区//////////////////////////////////

    /**
     * 获得单例
     */
    public static BDWidget getInstance() {
        return mInstance;
    }

}
