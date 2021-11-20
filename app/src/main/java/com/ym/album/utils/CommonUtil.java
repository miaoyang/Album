package com.ym.album.utils;

import android.os.Environment;

public class CommonUtil {
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}
