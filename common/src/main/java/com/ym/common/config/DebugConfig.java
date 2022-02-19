package com.ym.common.config;

import android.content.Context;

import com.ym.common.BuildConfig;

import java.io.File;

public class DebugConfig {
    // debug
    public static volatile boolean isDebug = false;
    // log
    public static volatile String logPath;
    // tag
    public static volatile String appTag = "";


    public static void setDebug(boolean flag){
        isDebug = flag;
    }

    public static String initLogDir(Context context) {
        File logDir;
        logDir = new File(context.getExternalFilesDir(null), "logs");
        logPath= logDir.getAbsolutePath();
        return logPath;
    }

    public static void setAppTag(String tag){
        appTag = tag;
    }
}
