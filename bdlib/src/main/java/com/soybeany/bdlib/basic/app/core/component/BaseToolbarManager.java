package com.soybeany.bdlib.basic.app.core.component;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.soybeany.bdlib.util.system.KeyboardUtils;

/**
 * 标准工具栏管理器需遵循的协议
 * <br>Created by Ben on 2016/7/19.
 */
public abstract class BaseToolbarManager {

    private static final int BASE_ID = 100; // 用于为id添加下限

    private Activity mActivity; // 活动页面

    protected BaseToolbarManager(Activity activity) {
        mActivity = activity;
    }

    /**
     * 创建选项菜单时的回调
     */
    public boolean onCreateOptionsMenu(ToolbarInfo info, Menu menu) {
        if (null == info) {
            return false;
        }
        ToolbarInfo.MenuItemInfo[] menuItemArr = info.menuItemInfoArr;
        if (null != menuItemArr) {
            int count = menuItemArr.length;
            for (int i = 0; i < count; i++) {
                ToolbarInfo.MenuItemInfo itemInfo = menuItemArr[i];
                if (null == itemInfo) {
                    continue;
                }
                MenuItem item = menu.add(Menu.NONE, BASE_ID + i, Menu.NONE, itemInfo.title);
                item.setIcon(itemInfo.iconResId);
                item.setShowAsAction(itemInfo.actionEnum);
            }
        }
        return false;
    }

    /**
     * 选择列表项时的回调
     */
    public boolean onOptionsItemSelected(ToolbarInfo info, MenuItem item) {
        if (null == info) {
            return false;
        }
        int id = item.getItemId();
        int index = id - BASE_ID;
        ToolbarInfo.MenuItemInfo[] menuItemArr = info.menuItemInfoArr;
        ToolbarInfo.IItemListener menuItemListener = info.menuItemListener;

        if (android.R.id.home == id) {
            ToolbarInfo.IItemListener backItemListener = info.backItemListener;
            if (null != backItemListener) {
                KeyboardUtils.closeKeyboard(mActivity);
                backItemListener.itemOnClick(0, null);
            }
        } else if (null != menuItemArr && null != menuItemListener && 0 <= index && menuItemArr.length > index) {
            menuItemListener.itemOnClick(index, menuItemArr[index]);
        }
        return false;
    }

    /**
     * 将指定的Info设置到toolBar上
     */
    public abstract void update(ToolbarInfo info);

    /**
     * 获得工具栏
     *
     * @return 若非空，则注册为系统工具栏
     */
    public abstract Toolbar getToolbar();

}
