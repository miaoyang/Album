package com.ym.album.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/2/28 20:28
 */
public abstract class BaseMvpFragment<T extends BasePresenter> extends BaseFragment implements BaseView{
    protected T mPresenter;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroyView();
    }

}
