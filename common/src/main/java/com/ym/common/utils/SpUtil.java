package com.ym.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * Author:yangmiao
 * Desc:
 * Time:2022/1/27 20:11
 */
public class SpUtil {
    private static volatile SpUtil sInstance;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    private static final String SP_FILE = "Album";

    private SpUtil(Context context){
        sp = context.getSharedPreferences(SP_FILE,Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static SpUtil getInstance(@NotNull Context context){
        if (sInstance == null){
            synchronized (SpUtil.class){
                if (sInstance == null){
                    sInstance = new SpUtil(context);
                }
            }
        }
        return sInstance;
    }

    public void putString(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }

    public String getString(String key,String defaultValue){
        return sp.getString(key,defaultValue);
    }

    public void putInt(String key,Integer value){
        editor.putInt(key,value);
        editor.commit();
    }

    public Integer getInt(String key,Integer defaultValue){
        return sp.getInt(key,defaultValue);
    }

    public void putLong(String key,Long value){
        editor.putLong(key,value);
        editor.commit();
    }

    public Long getLong(String key,Long defaultValue){
        return sp.getLong(key,defaultValue);
    }

    public void putBoolean(String key,Boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    public Boolean getBoolean(String key,Boolean defaultValue){
        return sp.getBoolean(key,defaultValue);
    }

    public void putBoolean(String key,float value){
        editor.putFloat(key,value);
        editor.commit();
    }

    public float getBoolean(String key,float defaultValue){
        return sp.getFloat(key,defaultValue);
    }

}
