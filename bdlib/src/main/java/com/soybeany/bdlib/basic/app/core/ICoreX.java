package com.soybeany.bdlib.basic.app.core;

import com.soybeany.bdlib.basic.app.core.component.BaseBusiness;
import com.soybeany.bdlib.basic.app.core.component.BaseViewHolder;
import com.soybeany.bdlib.basic.app.core.interfaces.IDestroyListener;
import com.soybeany.bdlib.basic.app.core.interfaces.IFragmentResultListener;
import com.soybeany.bdlib.basic.app.core.interfaces.IIdentifier;
import com.soybeany.bdlib.basic.app.core.interfaces.IPermissionChecker;
import com.soybeany.bdlib.basic.app.core.interfaces.IProgressDialogKeeper;
import com.soybeany.bdlib.basic.app.core.interfaces.IRefreshable;
import com.soybeany.bdlib.basic.app.core.interfaces.ITemplateUser;
import com.soybeany.bdlib.basic.app.core.interfaces.IToastHolder;
import com.soybeany.bdlib.basic.app.core.interfaces.IToolbarOwner;
import com.soybeany.bdlib.util.system.PermissionRequester;

/**
 * 跨越的核心，使得Activity与Fragment上的编码体验，在一般开发上得以保持一致
 * <br>Created by Soybeany on 2017/3/13.
 */
public interface ICoreX<Data, ViewHolder extends BaseViewHolder, Business extends BaseBusiness> extends
        IIdentifier, IRefreshable, IDestroyListener, IFragmentResultListener,
        ITemplateUser<Data, ViewHolder, Business>, IPermissionChecker,
        PermissionRequester.IPermissionDealer,
        PermissionRequester.IPermissionCallback,
        IProgressDialogKeeper, IToastHolder, IToolbarOwner {

}
