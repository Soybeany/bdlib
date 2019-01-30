package com.soybeany.bdlib.basic.database.output.dialog;

import com.soybeany.bdlib.util.async.impl.dialog.SimpleProgressDialogWrapper;
import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;

/**
 * 数据库操作弹窗包装器
 * <br>Created by Soybeany on 2017/8/29.
 */
public class DatabaseDialogWrapper extends SimpleProgressDialogWrapper<Object> {

    public DatabaseDialogWrapper(IProgressDialogHolder holder) {
        super(holder, null);
        cancelable(false);
    }

}
