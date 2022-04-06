package com.ym.album.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ym.common_util.utils.LogUtil;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/4/1 17:25
 */
public class CustomViewPager extends ViewPager {
    private static final String TAG = "CustomViewPager";
    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        }catch (Exception e){
            LogUtil.e(TAG,"onTouchEvent(): error ",e);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        }catch (Exception e){
            LogUtil.e(TAG,"onInterceptTouchEvent(): error ",e);
        }
        return false;
    }

    
}
