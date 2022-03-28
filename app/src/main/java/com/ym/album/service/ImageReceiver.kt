package com.ym.album.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ym.common_util.utils.LogUtil

/**
 * TODO 当有图片更新时，刷新图像存储库
 */
class ImageReceiver : BroadcastReceiver() {
    companion object{
        private const val TAG = "ImageReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        LogUtil.d(TAG,"action=$action")
    }
}