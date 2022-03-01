package com.ym.album.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/2/28 20:25
 */
public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity implements BaseView{
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null){
            mPresenter.detachView();
        }
        super.onDestroy();
    }


}
