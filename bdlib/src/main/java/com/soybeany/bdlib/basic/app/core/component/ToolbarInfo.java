package com.soybeany.bdlib.basic.app.core.component;

import java.io.Serializable;

/**
 * <br>Created by Ben on 2016/7/18.
 */
@SuppressWarnings("unchecked")
public class ToolbarInfo<Info extends ToolbarInfo> implements Cloneable, Serializable {

    /**
     * 默认toolbar标题
     */
    public static String DEFAULT_TITLE = "请设置标题";

    /**
     * 首页
     */
    public static String ITEM_TEXT_HOME = "首页";

    /**
     * 返回
     */
    public static String ITEM_TEXT_BACK = "返回";

    /**
     * 工具栏信息（作Key使用）
     */
    public static String KEY_TOOLBAR_INFO = "bd_keyToolbarInfo";

    /**
     * 工具栏是否可见
     */
    public boolean isToolbarVisible = true;

    /**
     * 返回按钮是否可见
     */
    public boolean isBackItemVisible = true;

    /**
     * 标题
     */
    public String title = DEFAULT_TITLE;

    /**
     * 副标题
     */
    public String subtitle;

    /**
     * 自定义视图的资源id
     */
    public int customViewResId;

    /**
     * 菜单按钮信息数组
     */
    public transient MenuItemInfo[] menuItemInfoArr;

    /**
     * 返回按钮监听器
     */
    public transient IItemListener backItemListener;

    /**
     * 菜单按钮监听器
     */
    public transient IItemListener menuItemListener;


    public ToolbarInfo() {

    }

    public ToolbarInfo(String title) {
        title(title);
    }

    /**
     * 设置工具栏是否可见
     */
    public Info toolbarVisible(boolean visible) {
        isToolbarVisible = visible;
        return (Info) this;
    }

    /**
     * 设置返回按钮是否可见
     */
    public Info backItemVisible(boolean visible) {
        isBackItemVisible = visible;
        return (Info) this;
    }

    /**
     * 设置标题
     */
    public Info title(String desc) {
        title = desc;
        return (Info) this;
    }

    /**
     * 设置副标题
     */
    public Info subtitle(String desc) {
        subtitle = desc;
        return (Info) this;
    }

    /**
     * 设置自定义视图资源id
     */
    public Info customViewResId(int resId) {
        customViewResId = resId;
        return (Info) this;
    }

    /**
     * 设置菜单列表项信息
     */
    public void setMenuItemInfo(MenuItemInfo... info) {
        menuItemInfoArr = info;
    }

    /**
     * 复制对象信息
     */
    public void copy(ToolbarInfo info) {
        if (null == info) {
            return;
        }
        isToolbarVisible = info.isToolbarVisible;
        isBackItemVisible = info.isBackItemVisible;
        title = info.title;
        subtitle = info.subtitle;
        customViewResId = info.customViewResId;
    }


    // ******************************接口部分******************************

    /**
     * item的监听
     */
    public interface IItemListener {

        /**
         * 被点击回调
         */
        void itemOnClick(int index, MenuItemInfo info);
    }


    // ******************************静态内部类部分******************************

    /**
     * 菜单项
     */
    public static class MenuItemInfo {
        /**
         * 图标资源
         */
        public int iconResId;

        /**
         * 标题
         */
        public String title;

        /**
         * 显示模式
         */
        public int actionEnum = android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM;

        /**
         * 设置图标资源
         */
        public MenuItemInfo icon(int iconResId) {
            this.iconResId = iconResId;
            return this;
        }

        /**
         * 设置标题
         */
        public MenuItemInfo title(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置显示模式
         */
        public MenuItemInfo action(int actionEnum) {
            this.actionEnum = actionEnum;
            return this;
        }
    }
}
