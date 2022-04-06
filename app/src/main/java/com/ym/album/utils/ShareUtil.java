package com.ym.album.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.ym.album.AlbumApp;
import com.ym.album.ui.bean.AppInfoItemBean;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/4/6 11:36
 */
public class ShareUtil {
    /**
     * 查询已安装的app
     * @param context
     * @return
     */
    public static List<AppInfoItemBean> getAllApps(final Context context){
        List<AppInfoItemBean> lists = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (int i=0,len = packageInfos.size();i<len;i++){
            PackageInfo packageInfo = packageInfos.get(i);
            AppInfoItemBean bean = new AppInfoItemBean(
                    packageInfo.applicationInfo.loadLabel(packageManager).toString(),
                    packageInfo.packageName,
                    packageInfo.versionName,
                    packageInfo.versionCode,
                    "",
                    packageInfo.applicationInfo.loadIcon(packageManager)
            );
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                //排除当前应用（替换成你的应用包名即可）
                if(!packageInfo.packageName.equals("com.ym.album")) {
                    lists.add(bean);
                }
            }

        }
        return lists;
    }

    public static String saveBitmapToCache(Bitmap bitmap)  {
        File file = new File(AlbumApp.getApp().getCacheDir(),System.currentTimeMillis()+".png");
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }
}
