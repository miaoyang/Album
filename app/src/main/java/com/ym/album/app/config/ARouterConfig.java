package com.ym.album.app.config;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ym.common_util.config.DebugConfig;


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
