package com.ym.album.ui.activity.account;

import com.ym.album.base.BasePresenter;
import com.ym.album.ui.activity.account.AccountContract;
import com.ym.album.ui.activity.account.AccountModel;
import com.ym.album.net.RxScheduler;
import com.ym.album.net.bean.BaseBean;
import com.ym.album.room.model.UserInfo;
import com.ym.common_util.utils.LogUtil;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/1 11:25
 */
public class AccountPresenter extends BasePresenter<AccountContract.View> implements AccountContract.Presenter {
    private static final String TAG = "AccountPresenter";

    private AccountContract.Model mAccountModel;
    public AccountPresenter(){
        this.mAccountModel = new AccountModel();
    }
    @Override
    public void login(String username, String password) {
        if (!isViewAttached()){
            LogUtil.d(TAG,"login(): view is not attached!");
            return;
        }
        mAccountModel.login(username,password)
                .compose(RxScheduler.Obs_io_main())
                .subscribe(new Observer<BaseBean<UserInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseBean<UserInfo> userInfoBaseBean) {
                        LogUtil.d(TAG,"login(): onNext "+userInfoBaseBean.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e(TAG,"login(): msg="+e.toString(),e);
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG,"login(): onComplete");
                    }
                });
    }
}
