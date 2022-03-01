package com.ym.common_util.utils;

import android.content.Context;
import android.os.Environment;
import android.util.LruCache;

import java.io.File;


public class DataCacheUtil {

//    private DiskLruCache mDiskLruCache = null;

    /**
     * 磁盘缓存
     * @param context
     */
    public void getDiskCache(final Context context) {
        File cacheDir = getDiskCacheDir(context, "object");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        //...
    }

    /**
     * 内存缓存
     */
    private LruCache<String, Object> mVideoCache;


    /**
     * 添加进入缓存列表
     *
     * @param key
     * @param value
     */
    public void addVideoCache(String key, Object value) {
        mVideoCache.put(key, value);
    }

    /**
     * 从缓存列表中拿出来
     *
     * @param key
     * @return
     */
    public Object getVideoCache(String key) {
        return mVideoCache.get(key);
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
}
