package com.ym.album;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ym.album.app.config.ARouterConfig;
import com.ym.common.utils.LogUtil;

public class AlbumApp extends Application {
    private static final String TAG = "AlbumApp";
    private static AlbumApp sInstance;

    public AlbumApp(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ARouterConfig.init(this);
    }

    public static AlbumApp getApp(){
        return sInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
        LogUtil.e(TAG,"onTerminate(): ARouter destroy");
    }
}
