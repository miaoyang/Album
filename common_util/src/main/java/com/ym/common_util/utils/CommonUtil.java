package com.ym.common_util.utils;

import android.content.Context;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import java.io.File;

public class CommonUtil {
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public static File createFile(Context context,String name){
        if (hasSdcard()){
            File[] dirs = ContextCompat.getExternalFilesDirs(context,null);
            if (dirs != null && dirs.length>0){
                File dir = dirs[0];
                return new File(dir,name);
            }
        }
        return null;
    }
}
