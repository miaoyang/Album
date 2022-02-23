package com.ym.album.ui.activity.image

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ym.album.R
import com.ym.album.app.config.PathConfig
import com.ym.album.ui.BaseActivity
import com.ym.album.ui.activity.Constant
import com.ym.common.utils.LogUtil
import com.ym.common.utils.TimeUtil
import java.util.*

@Route(path = PathConfig.Image.IMAGE_CLICK)
class ImageClickActivity : BaseActivity(),View.OnClickListener {
    private lateinit var mContext:Context

    private var mIvImageBackup:ImageView? = null
    private var mTvYearMonthDay:TextView? = null
    private var mTvHourMin:TextView? = null
    private var mIvImageSwitch:ImageView? = null

    private var mIvMiddleImage:ImageView? = null

    private var mIvSend:ImageView? = null
    private var mIvEdit:ImageView? = null
    private var mIvCollect:ImageView? = null
    private var mIvDelete:ImageView? = null
    private var mIvMore:ImageView? = null

    @Autowired(name = Constant.Image.clickItemImage)
    @JvmField
    var clickItemImage:String= ""

    companion object{
        private const val TAG = "ImageClickActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_click)
        mContext = this
        initView()
        handlerData()
    }

    override fun onClick(p: View?) {
        when(p?.id){
            R.id.iv_image_backup ->{
                ARouter.getInstance().build(PathConfig.HOME.MAIN_ACTIVITY).navigation()
            }
            R.id.iv_switch_landscape ->{
                // TODO
                LogUtil.d(TAG,"onClick(): switch landscape")
            }

            R.id.iv_image_send ->{
                LogUtil.d(TAG,"onClick(): send")
            }
            R.id.iv_image_edit ->{
                LogUtil.d(TAG,"onClick(): edit")
            }
            R.id.iv_image_collect ->{
                LogUtil.d(TAG,"onClick(): collect")
            }
            R.id.iv_image_delete ->{
                LogUtil.d(TAG,"onClick(): delete")
            }
            R.id.iv_image_more ->{
                LogUtil.d(TAG,"onClick(): more")
            }

        }
    }

    private fun initView(){
        mIvImageBackup = findViewById(R.id.iv_image_backup)
        mTvYearMonthDay = findViewById(R.id.tv_year_month_day)
        mTvHourMin = findViewById(R.id.tv_hour_min)
        mIvImageSwitch = findViewById(R.id.iv_switch_landscape)

        mIvMiddleImage = findViewById(R.id.iv_click_image)

        mIvSend = findViewById(R.id.iv_image_send)
        mIvEdit = findViewById(R.id.iv_image_edit)
        mIvCollect = findViewById(R.id.iv_image_collect)
        mIvDelete = findViewById(R.id.iv_image_delete)
        mIvMore = findViewById(R.id.iv_image_more)
    }

    private fun handlerData(){
        val date = Date(System.currentTimeMillis())
        mTvYearMonthDay?.setText(TimeUtil.dateToStr(date,"yyyy年MM月dd日"))
        mTvHourMin?.setText(TimeUtil.dateToStr(date,"HH:mm"))

        mIvMiddleImage?.let { Glide.with(mContext).load(clickItemImage).into(it) }

    }


}