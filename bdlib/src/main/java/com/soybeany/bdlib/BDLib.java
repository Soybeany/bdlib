package com.soybeany.bdlib;

import android.app.Application;
import android.content.Context;

import com.soybeany.bdlib.basic.app.BDApp;
import com.soybeany.bdlib.basic.database.BDDatabase;
import com.soybeany.bdlib.basic.network.BDNetwork;
import com.soybeany.bdlib.basic.widget.BDWidget;
import com.soybeany.bdlib.util.BDUtil;
import com.soybeany.bdlib.util.context.BDContext;

/**
 * BD系列库
 * <br>概述：
 * <br>1.为应用搭建提供便捷的解决方案，主要提供了4个方面的封装（应用、视图、数据库、网络请求）
 * <br>2.可作为1方库（小型项目，常用extension中的api）直接调用；也可作为2、3方库（中大型项目，2方允许修改，3方不允许修改）供项目1方库底层调用
 * <p>
 * <br>库结构：
 * <br>1.basic（基础子功能）：含有多个相互独立的子功能，提供基本的功能实现
 * <br>2.extension（拓展功能）：将basic中的子功能组合使用，提供高级的功能实现
 * <br>3.util（公用资源）：可供各个子功能自由调用的资源
 * <br>4.drawable（视图资源，视图子功能使用）：res目录下，基础资源
 * <br>5.values（值资源，应用、视图子功能使用）：res目录下，基础资源
 * <p>
 * <br>基础子功能的模块结构：
 * <br>1.一般都具有"interfaces"、"util"、"core"、"output"这4个基础包，其依赖关系为："output"->"core"->"util"->"interfaces"
 * <br>2."interfaces"(协议包):子模块所遵循的基本协议，可供外部自定义实现
 * <br>3."util"(工具包):子模块中的可共用部分，一般不供外部使用
 * <br>4."core"(核心包):处理该子模块的内部逻辑，一般不供外部使用
 * <br>5."output"(输出包):与外部交互，其内部会调用"core"中的逻辑
 * <p>
 * <br>拓展功能的模块结构
 * <br>1.function(功能)：整合基础子功能，所得到的新功能
 * <br>2.module(模块)：提供常用的模块解决方案，如登录模块
 * <p>
 * <br>常用前缀命名：
 * <br>1.base:本类库中最底层的实现，拓展性最强，一般不在项目中直接使用
 * <br>2.simple:对base进行了一层封装，拓展性/可用性强，可以在项目中直接/拓展使用
 * <br>3.std:对simple再进行了一层封装，可用性最强，在项目中直接使用（或作为参考），非常适用于简单的小项目
 * <p>
 * <br>drawable资源命名：
 * <br>1.文件命名：全小写，系列类库（默认bd）_类别_用途/形状描述_补充说明1_补充说明2_……
 * <br>2.类别：sh(形状)[shape]、sl(选择器)[selector]、ll(层列表)[layer-list]、ic(图标)[vector]
 * <br>3.用途/形状描述：ic时指定形状，其它时候(sh、sl)指定用途，常用：bdg(标记)、bg(背景，sl时可加ck)、btn(按钮)、txt(文本)、seg(分段)
 * <br>4.补充说明：用以作细节区分，顺序一般为：[方向]、[颜色]、[边框]、[圆角]
 * <br>5.连接符：不同部分使用“_”连接，相同部分的补充使用“2”连接（如bg2ck）
 * <br>6.描述的常用补充：ck(可点击)、lk(链接)、s(选择)、us(未选择)
 * <br>7.说明的常用补充：n(普通)、p(按下)
 * <p>
 * <br>layout资源命名：
 * <br>1.文件命名：全小写，系列类库（默认bd）_子模块（app:应用、wdg:视图）_视图类型（activity、layout、view）_特征补充说明
 * <p>
 * <br>values资源命名：
 * <br>1.文件命名：类别（参照安卓官方）_范围（bd:本类库；std:外部标准；app:应用；wdg:视图）
 * <br>2.内部命名：采用驼峰（首字母也可大写）、全小写加“_”分隔
 * <p>
 * <br>widget常用组件简写：
 * <br>LinearLayout : ll
 * <br>RelativeLayout : rl
 * <br>TableLayout : tl
 * <br>FrameLayout : fl
 * <br>Button : btn
 * <br>ImageView : iv
 * <br>ScrollView : sc
 * <br>TextView : tv
 * <br>ListView : lv
 * <br>EditText : et
 * <br>CheckBox : cb
 * <br>ImageButton : i-btn
 * <br>RadioButton : r-btn
 * <br>ProgressBar : p-bar
 * <br>Created by Soybeany on 2017/2/5.
 */
public class BDLib {

    private static BDLib mInstance = new BDLib(); // 单例

    private BDLib() {

    }

    // //////////////////////////////////静态方法区//////////////////////////////////

    /**
     * 初始化BDLib（一般在Application的onCreate中调用）
     */
    public static BDLib init(Application application) {
        Context context = application.getApplicationContext();
        // 设置全局的上下文
        BDContext.setContext(context);
        // 初始化子模块
        BDDatabase.init(context);
        return mInstance;
    }

    /**
     * 释放BDLib占用的资源（一般在Application的onTerminate中调用）
     */
    public static void release() {
        BDNetwork.release();
        BDDatabase.release();
    }


    // //////////////////////////////////子模块自定义配置//////////////////////////////////

    /**
     * 应用的配置
     */
    public BDApp appConfig() {
        return BDApp.getInstance();
    }

    /**
     * 网络请求配置
     */
    public BDNetwork nwConfig() {
        return BDNetwork.getInstance();
    }

    /**
     * 数据库配置
     */
    public BDDatabase dbConfig() {
        return BDDatabase.getInstance();
    }

    /**
     * 视图配置
     */
    public BDWidget wdgConfig() {
        return BDWidget.getInstance();
    }

    /**
     * 工具类配置
     */
    public BDUtil utilConfig() {
        return BDUtil.getInstance();
    }

}
