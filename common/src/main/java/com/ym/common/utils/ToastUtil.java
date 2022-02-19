package com.ym.common.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void showShort(Context context,CharSequence c){
        Toast.makeText(context,c,Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context,CharSequence c){
        Toast.makeText(context,c,Toast.LENGTH_LONG).show();
    }

}
