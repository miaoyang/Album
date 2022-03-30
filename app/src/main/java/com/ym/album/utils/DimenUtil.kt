package com.ym.album.utils

import android.content.Context
import android.graphics.Point
import android.os.Build
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

}