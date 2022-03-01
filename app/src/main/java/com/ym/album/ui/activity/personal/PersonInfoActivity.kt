package com.ym.album.ui.activity.personal

import android.os.Bundle
import android.widget.ImageView
import butterknife.ButterKnife
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ym.album.R
import com.ym.album.app.config.PathConfig
import com.ym.album.base.BaseActivity
import com.ym.common_util.utils.LogUtil

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
        initData()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_person_info
    }

    override fun initView(){
        mIvHeadBackup = findViewById(R.id.iv_person_info_backup)
    }

    override fun initData() {
        mIvHeadBackup?.setOnClickListener {
            LogUtil.d(TAG,"handlerData(): ARouter before")
            ARouter.getInstance().build(PathConfig.HOME.MAIN_ACTIVITY).navigation()
            LogUtil.d(TAG,"handlerData(): ARouter after")
        }
    }
}