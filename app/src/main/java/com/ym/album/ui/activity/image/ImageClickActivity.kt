package com.ym.album.ui.activity.image

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ym.album.R
import com.ym.album.app.config.AppConstant
import com.ym.album.app.config.PathConfig
import com.ym.album.base.BaseActivity
import com.ym.album.ui.adapter.ImagePageAdapter
import com.ym.album.ui.bean.ImageItemBean
import com.ym.album.ui.widget.CustomDialog
import com.ym.album.ui.widget.CustomViewPager
import com.ym.album.ui.widget.SharePopupWindow
import com.ym.album.utils.DimenUtil
import com.ym.common_util.utils.LogUtil
import com.ym.common_util.utils.TimeUtil
import java.util.*
import kotlin.collections.ArrayList

@Route(path = PathConfig.Image.IMAGE_CLICK)
class ImageClickActivity : BaseActivity(),View.OnClickListener,ImagePageAdapter.OnPageClickListener {
    private lateinit var mContext:Context

    private var mIvImageBackup:ImageView? = null
    private var mTvYearMonthDay:TextView? = null
    private var mTvHourMin:TextView? = null
    private var mIvImageSwitch:ImageView? = null

    private var mIvMiddleImage:ImageView? = null

    private lateinit var mSendLayout:LinearLayout
    private lateinit var mEditLayout:LinearLayout
    private lateinit var mCollectLayout:LinearLayout
    private lateinit var mDeleteLayout:LinearLayout
    private lateinit var mMoreLayout:LinearLayout

    private lateinit var mCVPImage:CustomViewPager
    private lateinit var imagePageAdapter: ImagePageAdapter

    // 所点击的当前图片
    @Autowired(name = AppConstant.UI.CLICK_IMAGE_ITEM)
    @JvmField
    var clickItemImage:Int= 0

    @Autowired(name = AppConstant.UI.DATA_IMAGE_ITEM_LIST)
    @JvmField
    var imageItemList:ArrayList<ImageItemBean>? = null

    companion object{
        private const val TAG = "ImageClickActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_click)
        mContext = this
        initView()
        initData()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_image_click
    }

    override fun onClick(p: View?) {
        when(p){
            mIvImageBackup ->{
                LogUtil.d(TAG,"onClick(): backup to image home")
                ARouter.getInstance().build(PathConfig.HOME.MAIN_ACTIVITY).navigation()
            }
            mIvImageSwitch ->{
                // TODO
                LogUtil.d(TAG,"onClick(): switch landscape")
            }

            mSendLayout ->{
                showShareDialog()
                LogUtil.d(TAG,"onClick(): send")
            }
            mEditLayout ->{
                LogUtil.d(TAG,"onClick(): edit")
            }
            mCollectLayout->{
                LogUtil.d(TAG,"onClick(): collect")
            }
            mDeleteLayout ->{
                showDeleteDialog()
                LogUtil.d(TAG,"onClick(): delete")
            }
            mMoreLayout ->{
                LogUtil.d(TAG,"onClick(): more")
            }

        }
    }

    override fun initView(){
        mIvImageBackup = findViewById(R.id.iv_image_backup)
        mIvImageBackup?.setOnClickListener(this)
        mTvYearMonthDay = findViewById(R.id.tv_year_month_day)
        mTvHourMin = findViewById(R.id.tv_hour_min)
        mIvImageSwitch = findViewById(R.id.iv_switch_landscape)

        mIvMiddleImage = findViewById(R.id.iv_click_image)

        mSendLayout = findViewById(R.id.image_bottom_send)
        mSendLayout.setOnClickListener(this)
        mEditLayout = findViewById(R.id.image_bottom_edit)
        mEditLayout.setOnClickListener(this)
        mCollectLayout = findViewById(R.id.image_bottom_collect)
        mCollectLayout.setOnClickListener(this)
        mDeleteLayout = findViewById(R.id.image_bottom_delete)
        mDeleteLayout.setOnClickListener(this)
        mMoreLayout = findViewById(R.id.image_bottom_more)
        mMoreLayout.setOnClickListener(this)


        mCVPImage = findViewById(R.id.cvp_image)
        imagePageAdapter = ImagePageAdapter(this, imageItemList)
        imagePageAdapter.setOnPageClickListener(this)
//        val params:RelativeLayout.LayoutParams = mCVPImage.layoutParams as RelativeLayout.LayoutParams
//        params.width = RelativeLayout.LayoutParams.WRAP_CONTENT
//        params.height = RelativeLayout.LayoutParams.WRAP_CONTENT
//        mCVPImage.layoutParams = params
        mCVPImage.adapter = imagePageAdapter
        mCVPImage.setCurrentItem(clickItemImage,false)
    }

    override fun initData() {

    }

    override fun pageSlide(position: Int) {
        val item:ImageItemBean? = imageItemList?.get(position)
        item?.addTime = item?.addTime?.times(1000)
//        LogUtil.d(TAG,"pageSlide(): add time ${item?.addTime}")
        item.apply {
            val date = this?.addTime?.let { Date(it) }
            mTvYearMonthDay?.text = TimeUtil.dateToStr(date, "yyyy年MM月dd日")
            mTvHourMin?.text = TimeUtil.dateToStr(date,"HH:mm")
        }
    }


    private fun showShareDialog(){
        val view = LayoutInflater.from(this).inflate(R.layout.share_dialog_layout,null)
        val imgPath = imageItemList?.get(clickItemImage)?.path

        val sharePopupWindow = SharePopupWindow(mContext)
        sharePopupWindow.setImagePath(imgPath)
        sharePopupWindow.showAtLocation(view,Gravity.BOTTOM,0,0)
        sharePopupWindow.height = DimenUtil.dp2px(this,200)
    }


    private fun showDeleteDialog(){
        val view:View = LayoutInflater.from(this).inflate(R.layout.select_image_dialog_delete_layout,null)
        val deleteButton:Button = view.findViewById(R.id.bt_show_delete)
        val quitButton:Button = view.findViewById(R.id.bt_show_quit)
        deleteButton.isPressed = true

        var dialog:CustomDialog?=null

        val listener:View.OnClickListener = View.OnClickListener { v ->
            when(v?.id){
                R.id.bt_show_quit ->{
                    dialog?.dismiss()
                }
                R.id.bt_show_delete ->{
                    deleteButton.isPressed = true
                    deleteButton.setTextColor(resources.getColor(R.color.white))
                }
            }
        }

        dialog = CustomDialog.Builder(this)
            .setView(view)
            .setCanTouchable(true)
            .setGravity(Gravity.BOTTOM)
            .setHeightDp(200)
            .setWidthLayoutParams(WindowManager.LayoutParams.MATCH_PARENT)
            .setStyle(R.style.Dialog)
            .setViewClickListen(R.id.bt_show_quit,listener)
            .setViewClickListen(R.id.bt_show_delete,listener)
            .build()
        dialog?.show()

    }
}