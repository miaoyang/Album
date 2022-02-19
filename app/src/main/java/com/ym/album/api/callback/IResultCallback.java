package com.ym.album.api.callback;

public interface IResultCallback<T> {
    void onSuccess(T o);
    void onFailed(T o);
}
