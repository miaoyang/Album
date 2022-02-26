package com.ym.album.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.ym.album.R;

/**
 * Author:yangmiao
 * Desc:
 * Time:2022/2/24 17:50
 */
public class LoadingImageView extends AppCompatImageView {
    public LoadingImageView(@NonNull Context context) {
        super(context);
    }

    @SuppressLint("ResourceType")
    public LoadingImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setImageResource(R.drawable.anim_yun);
        // 加载动画
        AnimationDrawable mAnimationDrawable = (AnimationDrawable) getDrawable();
        // 默认进入页面就开启动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
    }

}
