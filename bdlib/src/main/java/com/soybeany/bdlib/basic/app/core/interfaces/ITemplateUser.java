package com.soybeany.bdlib.basic.app.core.interfaces;

import com.soybeany.bdlib.basic.app.core.component.BaseBusiness;
import com.soybeany.bdlib.basic.app.core.component.BaseViewHolder;

/**
 * 模板使用者(模板方法模式)，编写新功能模块时使用的基本模板
 * <br>Created by Soybeany on 2017/3/12.
 */
public interface ITemplateUser<Data, ViewHolder extends BaseViewHolder, Business extends BaseBusiness> {

    /**
     * 初始化数据(高优先级，适合从数据集中读取并缓存数据)
     */
    void onInitData(Data data);

    /**
     * 设置布局的resId(根视图)
     */
    int setupLayoutResId();

    /**
     * 设置全局ViewHolder(一般返回一个新创建的视图持有器)
     */
    ViewHolder setupViewHolder();

    /**
     * 设置业务逻辑
     */
    Business setupBusiness();

    /**
     * 是否需要运行业务逻辑，若false则不调用{@link #doBusiness()}，且会结束当前页面
     */
    boolean needDoBusiness();

    /**
     * 处理业务逻辑
     */
    void doBusiness();

}
