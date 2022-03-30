package com.ym.album.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ym.album.utils.ImageMediaUtil
import com.ym.common_util.utils.LogUtil
import com.ym.common_util.utils.ThreadPoolUtil

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
        when(action){
            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE ->{
                // 重新扫描图像
                ThreadPoolUtil.diskExe{
                    ImageMediaUtil.getAlbumList(context)
                }
                ThreadPoolUtil.diskExe{
                    ImageMediaUtil.getImagePathList(context)
                }
            }
        }
    }
}