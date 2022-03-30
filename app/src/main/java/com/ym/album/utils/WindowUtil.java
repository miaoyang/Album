package com.ym.album.utils;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import com.ym.album.R;
import com.ym.common_util.utils.LogUtil;


/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/29 19:00
 */
public class WindowUtil {
    public static final String TAG = "WindowUtil";

    public static PopupWindow createPopupWindow(View parent,View v){
        v.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        PopupWindow popupWindow = new PopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,true);
        // 点击空白，隐藏掉window
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.popupAnim);
        popupWindow.setClippingEnabled(true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, DimenUtil.INSTANCE.getScreenHeightAndWidth(v.getContext()).y - v.getMeasuredHeight());
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                LogUtil.d(TAG,"createPopupWindow(): ");
            }
        });

        return popupWindow;
    }
}
