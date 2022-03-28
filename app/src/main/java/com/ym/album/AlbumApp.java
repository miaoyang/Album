package com.ym.album;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.weavey.loading.lib.LoadingLayout;
import com.ym.album.app.config.ARouterConfig;
import com.ym.album.app.config.AppConstant;
import com.ym.album.app.config.PathConfig;
import com.ym.album.event.Event;
import com.ym.album.event.EventBusUtil;
import com.ym.album.utils.ImageMediaUtil;
import com.ym.common_util.utils.CrashHandlerUtil;
import com.ym.common_util.utils.LogUtil;
import com.ym.common_util.utils.ThreadPoolUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        initLoading();
        CrashHandlerUtil.getInstance().init(this);
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

    private void initLoading() {
        LoadingLayout.getConfig()
                .setErrorText("出错啦~请稍后重试！")
                .setEmptyText("抱歉，暂无数据")
                .setNoNetworkText("无网络连接，请检查您的网络···")
                .setErrorImage(R.drawable.ic_empty_icon)
                .setEmptyImage(R.drawable.ic_empty_icon)
                .setNoNetworkImage(R.drawable.ic_no_network)
                .setAllTipTextColor(R.color.colorTabText)
                .setAllTipTextSize(14)
                .setReloadButtonText("点我重试哦")
                .setReloadButtonTextSize(14)
                .setReloadButtonTextColor(R.color.colorTabText)
                .setReloadButtonWidthAndHeight(150, 40)
                .setAllPageBackgroundColor(R.color.colorBackground)
                .setLoadingPageLayout(R.layout.default_loading_page);
    }
}
