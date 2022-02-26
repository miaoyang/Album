package com.ym.album.ui.fragment.person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ym.album.R
import com.ym.album.app.config.PathConfig
import com.ym.common.utils.LogUtil

@Route(path = PathConfig.Person.PERSON_HOME)
class PersonalFragment : Fragment() {
    var personQrCode:ImageView?=null

    companion object{
        private const val TAG = "PersonalFragment"
        @JvmStatic
        fun newInstance(): PersonalFragment? {
            return PersonalFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_personal, container, false)
        personQrCode = view.findViewById(R.id.iv_person_qr_code)

        personQrCode?.setOnClickListener {
            LogUtil.d(TAG,"onCreateView(): ARouter before")
            ARouter.getInstance().build(PathConfig.Person.PERSON_INFO).navigation()
            LogUtil.d(TAG,"onCreateView(): ARouter")
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    private fun initView(){
        personQrCode = view?.findViewById(R.id.iv_person_qr_code)!!
    }

}