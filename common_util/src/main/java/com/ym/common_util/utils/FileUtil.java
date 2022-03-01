package com.ym.common_util.utils;

import android.content.Context;

import java.io.File;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/2/28 21:15
 */
public class FileUtil {
    public static String getSDCardPath(Context context, String subPath,String appTag){
        String path = new File(context.getApplicationContext()
                .getExternalFilesDir(null),appTag+"/").getAbsolutePath();
        return new File(path,subPath).getAbsolutePath();
    }
}
