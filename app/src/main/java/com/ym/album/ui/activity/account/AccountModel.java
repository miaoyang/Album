package com.ym.album.ui.activity.account;

import com.ym.album.ui.activity.account.AccountContract;
import com.ym.album.net.ApiModule;
import com.ym.album.net.bean.BaseBean;
import com.ym.album.room.model.UserInfo;
import io.reactivex.rxjava3.core.Observable;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/1 11:23
 */
public class AccountModel implements AccountContract.Model {

    @Override
    public Observable<BaseBean<UserInfo>> login(String username, String password) {
        return ApiModule.getInstance().providerAccountService().login(username,password);
    }
}
