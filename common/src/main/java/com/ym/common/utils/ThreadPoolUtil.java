package com.ym.common.utils;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Author:yangmiao
 * Desc:
 * Time:2022/1/27 16:31
 */
public class ThreadPoolUtil {
    private static volatile ThreadPoolUtil sInstance;
    private static volatile HandlerThread sHandlerThread;
    private static Handler sHandler;

    private final Executor mNet;
    private final Executor mDisk;

    public static ThreadPoolUtil getInstance(){
        if (sInstance == null){
            synchronized (ThreadPoolUtil.class){
                if (sInstance == null){
                    sInstance = new ThreadPoolUtil();
                }
            }
        }
        return sInstance;
    }

    private ThreadPoolUtil(){
        mNet = Executors.newSingleThreadExecutor();
        mDisk = Executors.newFixedThreadPool(3);
    }

    public static void netExe(Runnable runnable){
        getInstance().mNet.execute(runnable);
    }

    public static void diskExe(Runnable runnable){
        getInstance().mDisk.execute(runnable);
    }

    public static Handler getHandler(){
        if (sHandlerThread == null){
            synchronized (ThreadPoolUtil.class){
                if (sHandlerThread == null){
                    sHandlerThread = new HandlerThread("HandlerThreadDefine");
                    sHandlerThread.start();
                    sHandler = new Handler(sHandlerThread.getLooper());
                }
            }
        }
        return sHandler;
    }
}
