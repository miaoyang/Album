package com.ym.album.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.WindowManager

object DimenUtil {
    /**
     * 获取屏幕的高度和宽度
     */
    fun getScreenHeightAndWidth(context: Context):Point{
        val wm:WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        wm.defaultDisplay.getRealSize(point)
        return point
    }

    /** 根据屏幕宽度与密度计算GridView显示的列数， 最少为三列，并获取Item宽度  */
    fun getImageItemWidth(activity: Activity): Int {
        val screenWidth = activity.resources.displayMetrics.widthPixels
        val densityDpi = activity.resources.displayMetrics.densityDpi
        var cols = screenWidth / densityDpi
        cols = cols.coerceAtLeast(3)
        val columnSpace = (2 * activity.resources.displayMetrics.density).toInt()
        return (screenWidth - columnSpace * (cols - 1)) / cols
    }

    /**
     * dp转px
     */
    fun dp2px(context: Context, dpValue: Int):Int{
        val scale = context.resources.displayMetrics.density;
        return (dpValue*scale+0.5f).toInt()
    }

    /**
     * px转dp
     */
    fun px2dp(context: Context,px:Float):Int{
        val scale = context.resources.displayMetrics.density
        return (px/scale+0.5f).toInt()
    }
}