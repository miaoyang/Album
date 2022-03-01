package com.ym.album.base;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/2/28 20:21
 */
public class BasePresenter<V extends BaseView> {
    protected V mView;

    public void attachView(V _view){
        this.mView = _view;
    }

    public void detachView(){
        this.mView = null;
    }

    public boolean isViewAttached(){
        return mView != null;
    }
}
