package com.soybeany.bdlib.basic.network.output.dialog;

import com.soybeany.bdlib.basic.network.interfaces.IRequest;
import com.soybeany.bdlib.util.async.impl.dialog.SimpleProgressDialogWrapper;
import com.soybeany.bdlib.util.async.interfaces.IProgressDialogHolder;

/**
 * 请求弹窗包装器(装饰者模式)
 * <br>Created by Soybeany on 2017/8/29.
 */
public class RequestDialogWrapper extends SimpleProgressDialogWrapper<IRequest> {

    public RequestDialogWrapper(IProgressDialogHolder holder, IRequest tag) {
        super(holder, tag);
    }

    @Override
    protected void onCancel(IRequest request) {
        super.onCancel(request);
        // 取消网络请求
        request.cancelRequest();
        // 弹出提示吐司
//        ToastUtils.show(StdHintUtils.STD_CANCEL_PREFIX + request.getStdHint());
    }

}
