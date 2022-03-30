package com.ym.album.ui.fragment.person

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ym.album.R
import com.ym.album.app.config.PathConfig
import com.ym.album.base.BaseFragment
import com.ym.common_util.utils.LogUtil


@Route(path = PathConfig.Person.PERSON_INFO_FRAGMENT)
class PersonalInfoFragment : BaseFragment() {
    companion object{
        private const val TAG = "PersonalInfoFragment"
    }
    private var mIvHeadBackup: ImageView?=null
    private var mIvPersonInfoImage: ImageView?=null
    private var mIvPersonInfoHeadBack: ImageView?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LogUtil.d(TAG,"onAttach():")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_personal_info, container, false)
        mIvHeadBackup = view.findViewById(R.id.iv_person_info_backup)
        mIvPersonInfoImage = view.findViewById(R.id.iv_person_info_head_image)
        mIvPersonInfoHeadBack = view.findViewById(R.id.iv_person_info_head_backup)
        LogUtil.d(TAG,"onCreateView():")
        initView()
        return view
    }

    override fun onDetach() {
        super.onDetach()
        LogUtil.d(TAG,"onDetach():")
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_personal_info
    }

    override fun initView() {
        mIvHeadBackup?.setOnClickListener {
            LogUtil.d(TAG,"initView(): ARouter before")
//            ARouter.getInstance().build(PathConfig.HOME.MAIN_ACTIVITY).navigation()
//            val fragment = ARouter.getInstance().build(PathConfig.Person.PERSON_HOME).navigation() as PersonalHomeFragment
//            switchFragment(R.id.fragment_vp,fragment)
            LogUtil.d(TAG,"initView(): ARouter after")
        }
        mIvPersonInfoHeadBack?.setOnClickListener {
            val fragment:HeadImageFragment = ARouter.getInstance().build(PathConfig.Person.PERSON_HEAD_IMAGE).navigation() as HeadImageFragment
            switchFragment(R.id.person_fragment,fragment)
            LogUtil.d(TAG,"initView(): path=${PathConfig.Person.PERSON_HEAD_IMAGE}")
        }
        mIvPersonInfoImage?.setOnClickListener {
            val fragment:HeadImageFragment = ARouter.getInstance().build(PathConfig.Person.PERSON_HEAD_IMAGE).navigation() as HeadImageFragment
            switchFragment(R.id.person_fragment,fragment)
            LogUtil.d(TAG,"initView(): path=${PathConfig.Person.PERSON_HEAD_IMAGE}")
        }
    }




}