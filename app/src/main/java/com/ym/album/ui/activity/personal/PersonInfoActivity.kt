package com.ym.album.ui.activity.personal

import android.os.Bundle
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ym.album.R
import com.ym.album.app.config.PathConfig
import com.ym.album.base.BaseActivity
import com.ym.album.ui.fragment.person.HeadImageFragment
import com.ym.album.ui.fragment.person.PersonalInfoFragment
import com.ym.common_util.utils.LogUtil

@Route(path = PathConfig.Person.PERSON_INFO_ACTIVITY)
class PersonInfoActivity : BaseActivity() {
    companion object{
        private const val TAG = "PersonInfoActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_info)
        initView()
        initData()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_person_info
    }

    override fun initView(){
        val fragment = ARouter.getInstance().build(PathConfig.Person.PERSON_INFO_FRAGMENT).navigation() as PersonalInfoFragment
        addFragment(R.id.person_fragment,fragment)
        LogUtil.d(TAG,"initView(): addFragment")
    }

    override fun initData() {

    }
}