package com.ym.common_res.util;

import android.app.Activity;
import android.view.View;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/1 19:32
 */
public class StatusBarUtil {
    /**
     * 隐藏状态栏
     */
    public static void hideStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * 显示状态栏
     */
    public static void showStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
