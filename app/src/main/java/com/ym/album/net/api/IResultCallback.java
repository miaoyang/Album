package com.ym.album.net.api;

public interface IResultCallback<T> {
    void onSuccess(T o);
    void onFailed(T o);
}
