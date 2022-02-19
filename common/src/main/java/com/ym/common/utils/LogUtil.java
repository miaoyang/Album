package com.ym.common.utils;

import android.util.Log;

import com.ym.common.config.DebugConfig;

public class LogUtil {
    private static final int POOL_SIZE = 10;
    private static final int MAX_CAPACITY = 512;
    private static final StringBuilder[] sb = new StringBuilder[POOL_SIZE];

    public static StringBuilder obtainStringBuilder(){
        StringBuilder ret = null;
        for (int i=0;i<POOL_SIZE;i++){
            if (sb[i] != null){
                ret = sb[i];
                sb[i] = null;
                ret.setLength(0);
                return ret;
            }
        }
        return new StringBuilder(MAX_CAPACITY);
    }

    public static String toStringAndRecycle(StringBuilder s){
        String ret = s.toString();
        for (int i=0;i<POOL_SIZE;i++){
            if (sb[i] == null){
                sb[i] = s;
                break;
            }
        }
        return ret;
    }

    public static void e(String tag,String msg){
        if (DebugConfig.isDebug){
            Log.e(DebugConfig.appTag+"--"+tag,msg);
        }
    }

    public static void e(String tag,String msg,Throwable e){
        if (DebugConfig.isDebug){
            Log.e(DebugConfig.appTag+"--"+tag,msg,e);
        }
    }

    public static void w(String tag,String msg){
        if (DebugConfig.isDebug){
            Log.w(DebugConfig.appTag+"--"+tag,msg);
        }
    }

    public static void w(String tag,String msg,Throwable e){
        if (DebugConfig.isDebug){
            Log.w(DebugConfig.appTag+"--"+tag,msg,e);
        }
    }

    public static void d(String tag,String msg){
        if (DebugConfig.isDebug){
            Log.d(DebugConfig.appTag+"--"+tag,msg);
        }
    }

    public static void d(String tag,String msg,Throwable e){
        if (DebugConfig.isDebug){
            Log.d(DebugConfig.appTag+"--"+tag,msg,e);
        }
    }
}
