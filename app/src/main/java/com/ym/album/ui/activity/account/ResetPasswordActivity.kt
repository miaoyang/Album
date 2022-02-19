package com.ym.album.ui.activity.account

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ym.album.ui.BaseActivity
import com.ym.album.R;
import com.ym.album.app.config.PathConfig
import com.ym.album.room.AppDataBase
import com.ym.album.room.model.UserInfo
import com.ym.album.ui.activity.Constant
import com.ym.common.utils.LogUtil
import com.ym.common.utils.ThreadPoolUtil
import com.ym.common.utils.ToastUtil

@Route(path = PathConfig.Account.RESET_PASSWORD)
class ResetPasswordActivity : BaseActivity() {
    companion object{
        private const val TAG="ResetPasswordActivity"
        public const val RESET_TELEPHONE = "reset_password_telephone"
    }

    @Autowired(name = RESET_TELEPHONE)
    @JvmField
    var resetTelephone: String =""

    private lateinit var mIvBackup:ImageView
    private lateinit var mEtResetPassword:EditText
    private lateinit var mEtRestPasswordAgain:EditText
    private lateinit var mBtFinish:Button

    private var mContext:Context?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        mContext = this
//        ARouter.getInstance().inject(this)
        initView()
        handlerData()
    }

    private fun initView(){
        mIvBackup = findViewById(R.id.reset_password_backup)
        mEtResetPassword = findViewById(R.id.et_reset_password)
        mEtRestPasswordAgain = findViewById(R.id.et_reset_password_again)
        mBtFinish = findViewById(R.id.bt_finish)
    }

    private fun handlerData(){
        mIvBackup.setOnClickListener {
            ARouter.getInstance().build(PathConfig.Account.FORGET_PASSWORD).navigation()
        }
        mBtFinish.setOnClickListener {
            val resetPsd = mEtResetPassword.text.toString()
            val resetPsdAgain = mEtRestPasswordAgain.text.toString()
            if (TextUtils.isEmpty(resetPsd)){
                ToastUtil.showShort(mContext,"输入的密码为空！")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(resetPsdAgain)){
                ToastUtil.showShort(mContext,"再次输入的密码为空！")
                return@setOnClickListener
            }
            if (resetPsd != resetPsdAgain){
                ToastUtil.showShort(mContext,"输入的密码不一致，请重新输入！")
                return@setOnClickListener
            }
            if (!TextUtils.isEmpty(resetTelephone)) {
                ThreadPoolUtil.diskExe {
                    val userInfo = AppDataBase.getInstance().userDao().selectByTelephone(resetTelephone)
                    if (userInfo != null){
                        userInfo.password=resetPsd
                        AppDataBase.getInstance().userDao().updateUserInfo(userInfo)
                        LogUtil.d(TAG,"handlerData(): update password success!")
                        ARouter.getInstance().build(PathConfig.Account.LOGIN)
                            .withString(Constant.Account.telephone,resetTelephone)
                            .withString(Constant.Account.password,resetPsd)
                            .navigation()
                    }
                }
            }
        }
    }
}