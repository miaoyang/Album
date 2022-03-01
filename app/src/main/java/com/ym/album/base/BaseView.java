package com.ym.album.base;

public interface BaseView {
    void showLoading();
    void hideLoading();
    void onError(String errorMsg);
}
