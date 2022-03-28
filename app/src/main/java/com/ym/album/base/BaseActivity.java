package com.ym.album.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;
import com.ym.album.R;
import com.ym.album.event.Event;
import com.ym.album.event.EventBusUtil;
import com.ym.album.utils.RequestPermissionsUtil;
import com.ym.common_util.config.DebugConfig;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/2/28 20:07
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        // app config
        initConfig();
        // ARouter
        ARouter.getInstance().inject(this);
        // status bar
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .statusBarDarkFont(true)
                .fitsSystemWindows(false)
                .init();
        EventBusUtil.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        RequestPermissionsUtil.requestPermission(requestCode,permissions,grantResults,this);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * get layout id
     * @return
     */
    public abstract int getLayoutId();

    /**
     * init view
     */
    public abstract void initView();

    /**
     * init data
     */
    public abstract void initData();

    private void initConfig(){
        DebugConfig.setDebug(true);
        DebugConfig.setAppTag("Album");
    }

    protected <T extends View> T f(int resId) {
        return (T) super.findViewById(resId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event){

    }

}
