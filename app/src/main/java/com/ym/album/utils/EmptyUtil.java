package com.ym.album.utils;

import android.text.TextUtils;

import java.util.List;

public class EmptyUtil {
    public static boolean strEmpty(String str){
        return TextUtils.isEmpty(str);
    }

    public static boolean strNotEmpty(String str){
        return !strEmpty(str);
    }

    public static boolean listEmpty(List list){
        if (list != null && list.size()>0){
            return true;
        }
        return false;
    }

    public static boolean listNotEmpty(List list){
        return !listEmpty(list);
    }
}
