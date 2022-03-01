package com.ym.album.ui.activity.account;


import com.ym.album.base.BaseView;
import com.ym.album.net.bean.BaseBean;
import com.ym.album.room.model.UserInfo;

import io.reactivex.rxjava3.core.Observable;


/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/2/28 20:33
 */
public interface AccountContract {
    interface Model{
        Observable<BaseBean<UserInfo>> login(String username, String password);
    }

    interface View extends BaseView{
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(String errMessage);

        void onSuccess();
    }

    interface Presenter {
        /**
         * 登陆
         *
         * @param username
         * @param password
         */
        void login(String username, String password);
    }
}
