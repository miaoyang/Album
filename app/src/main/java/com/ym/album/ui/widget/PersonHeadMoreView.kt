package com.ym.album.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.ym.album.R
import com.ym.album.app.config.AppConstant
import com.ym.album.event.Event
import com.ym.album.event.EventBusUtil
import com.ym.common_util.utils.LogUtil

/**
 *Author:yangmiao
 *Desc:
 *Time:2022/3/30 14:57
 */
class PersonHeadMoreView: LinearLayout {
    companion object{
        private const val TAG = "PersonHeadMoreView"
    }

    constructor(context: Context):super(context){
        initView(context)
    }

    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){
        initView(context)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val mTvDialogTakePhoto: TextView = findViewById(R.id.tv_dialog_take_photo)
        val mTvDialogFromAlbum: TextView = findViewById(R.id.tv_dialog_from_photo)
        val mTvDialogSave: TextView = findViewById(R.id.tv_dialog_save_photo)
        val mTvDialogExit: TextView = findViewById(R.id.tv_dialog_exit)

        mTvDialogExit.setOnClickListener {
            LogUtil.d(TAG, "init(): mTvDialogExit")
            EventBusUtil.sendEvent(Event<String>(AppConstant.Event.PERSON_DIALOG_EXIT,"dialog exit"))
        }

        mTvDialogTakePhoto.setOnClickListener {
            LogUtil.d(TAG, "init(): mTvDialogTakePhoto")
            EventBusUtil.sendEvent(Event<String>(AppConstant.Event.PERSON_DIALOG_TAKE_PHOTO,"take photo"))
        }

        mTvDialogFromAlbum.setOnClickListener {
            LogUtil.d(TAG, "init(): mTvDialogFromAlbum")
            EventBusUtil.sendEvent(Event<String>(AppConstant.Event.PERSON_DIALOG_OPEN_GALLERY,"open gallery"))
        }
    }

    fun initView(context: Context){
//        LayoutInflater.from(context).inflate(R.layout.person_dialog_layout, this)


    }

}