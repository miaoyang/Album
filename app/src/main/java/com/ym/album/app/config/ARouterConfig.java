package com.ym.album.app.config;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ym.common.config.DebugConfig;


public class ARouterConfig {
    public static void init(Application application){
        if (DebugConfig.isDebug){
            // open log
            ARouter.openLog();
            // install run
            ARouter.openDebug();
        }
        ARouter.init(application);
    }
}
