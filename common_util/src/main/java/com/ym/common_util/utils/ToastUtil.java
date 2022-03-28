package com.ym.common_util.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static long lastTime = 0L;
    private static String currentText="";
    private static final long MIN_SHOW_TIME = 500;

    public static void showShort(Context context,CharSequence c){
        if (!c.toString().equals(currentText) || (System.currentTimeMillis()-lastTime)>=MIN_SHOW_TIME){
            Toast.makeText(context,c,Toast.LENGTH_SHORT).show();
            currentText = c.toString();
        }
        lastTime=System.currentTimeMillis();
    }

    public static void showLong(Context context,CharSequence c){
        if (!c.toString().equals(currentText) || (System.currentTimeMillis()-lastTime)>=MIN_SHOW_TIME){
            Toast.makeText(context,c,Toast.LENGTH_LONG).show();
            currentText = c.toString();
        }
        lastTime=System.currentTimeMillis();
    }

}
