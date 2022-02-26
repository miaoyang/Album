package com.ym.album.ui.activity.personal

import android.os.Bundle
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ym.album.R
import com.ym.album.app.config.PathConfig
import com.ym.album.ui.BaseActivity
import com.ym.common.utils.LogUtil

@Route(path = PathConfig.Person.PERSON_INFO)
class PersonInfoActivity : BaseActivity() {
    companion object{
        private const val TAG = "PersonInfoActivity"
    }
    //@BindView(R.id.activity_person_info)
    var mIvHeadBackup:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_info)
        ButterKnife.bind(this)
        ARouter.getInstance().inject(this)
        initView()
        handlerData()
    }

    private fun initView(){
        mIvHeadBackup = findViewById(R.id.iv_person_info_backup)
    }

    private fun handlerData(){
        mIvHeadBackup?.setOnClickListener {
            LogUtil.d(TAG,"handlerData(): ARouter before")
            ARouter.getInstance().build(PathConfig.HOME.MAIN_ACTIVITY).navigation()
            LogUtil.d(TAG,"handlerData(): ARouter after")
        }
    }
}