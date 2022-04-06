package com.ym.album.ui.api;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

public interface ImageLoader {
    /**
     * 展示图片，方便自定义
     * @param context
     * @param path
     * @param imageView
     * @param width
     * @param height
     */
    void displayImage(Context context, String path, ImageView imageView, int width, int height);

    void displayImagePreview(Context context, String path, ImageView imageView, int width, int height);

    void clearMemoryCache();
}
