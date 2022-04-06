package com.ym.album.ui.api;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import com.ym.album.R;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/31 16:58
 */
public class GlideImageLoader implements ImageLoader {
    private static final String TAG = "GlideImageLoader";
    private static volatile GlideImageLoader sInstance;
    private GlideImageLoader(){}
    public static GlideImageLoader getInstance(){
        if (sInstance == null){
            synchronized (GlideImageLoader.class){
                if (sInstance==null){
                    sInstance = new GlideImageLoader();
                }
            }
        }
        return sInstance;
    }
    @Override
    public void displayImage(Context context, String path, ImageView imageView, int width, int height) {
        Glide.with(context)                             //配置上下文
                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .error(R.drawable.logo_big_ic)           //设置错误图片
                .placeholder(R.drawable.logo_big_ic)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Context context, String path, ImageView imageView, int width, int height) {
        Glide.with(context)                              //配置上下文
                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
