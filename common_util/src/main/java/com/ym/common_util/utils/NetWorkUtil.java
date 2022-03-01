package com.ym.common_util.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/2/28 21:32
 */
public class NetWorkUtil {
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
    }
}
