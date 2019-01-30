package com.soybeany.bdlib.basic.app;

/**
 * 应用模块（BD系列）
 * <br>概述：
 * <br>1.提供应用搭建过程中常用的底层架构，使应用的搭建更为便捷，维护更为容易
 * <br>2.使得在Activity与Fragment中编写界面、业务逻辑的体验一致，X系列（Cross）
 * <p>
 * <br>设计架构：
 * <br>1.架构模式：MVP模式，IViewHolder充当View层，Activity/Fragment充当Presenter层，Business充当Model层
 * <br>2.通讯方向：V<->P->M，即视图不能直接访问模型
 * <p>
 * <br>Interfaces：
 * <br>1.直接:作为使用者存在，持有或继承其它接口
 * <br>2.component：组件，使用者以持有方式使用
 * <br>3.cross：交叉接口，使用者以继承方式使用
 * <br>4.依赖关系：直接->component、cross
 * <br>Created by Soybeany on 2017/3/12.
 */
public class BDApp {

    private static BDApp mInstance = new BDApp(); // 单例

    private BDApp() {

    }


    // //////////////////////////////////静态方法区//////////////////////////////////

    /**
     * 获得单例
     */
    public static BDApp getInstance() {
        return mInstance;
    }

}
