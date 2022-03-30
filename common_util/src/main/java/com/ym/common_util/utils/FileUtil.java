package com.ym.common_util.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/2/28 21:15
 */
public class FileUtil {
    private static final String TAG = "FileUtil";
    public static String getSDCardPath(Context context, String subPath,String appTag){
        String path = new File(context.getApplicationContext()
                .getExternalFilesDir(null),appTag+"/").getAbsolutePath();
        return new File(path,subPath).getAbsolutePath();
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     * @return 创建的图片文件
     */
    public static File createImageFile(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName,".jpg",storageDir);
        } catch (IOException e) {
            LogUtil.e(TAG,"createImageFile(): error ",e);
        }
        return imageFile;
    }
}
