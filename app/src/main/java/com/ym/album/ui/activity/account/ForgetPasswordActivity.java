package com.ym.album.ui.activity.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ym.album.app.config.PathConfig;
import com.ym.album.room.AppDataBase;
import com.ym.album.room.model.UserInfo;
import com.ym.album.ui.BaseActivity;
import com.ym.album.R;
import com.ym.common.utils.ThreadPoolUtil;
import com.ym.common.utils.ToastUtil;
import com.ym.common.utils.ValidatorUtil;

@Route(path = PathConfig.Account.FORGET_PASSWORD)
public class ForgetPasswordActivity extends BaseActivity {
    private static final String TAG = "ForgetPasswordActivity";

    private ImageView mIvBackup;
    private EditText mEtInputPhone;
    private EditText mEtInputVerifyCode;
    private Button mBtSendVerifyCode;
    private Button mBtNextStep;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mContext = this;

        initView();
        handlerData();
    }

    private void initView(){
        mIvBackup = findViewById(R.id.forget_password_backup);
        mEtInputPhone = findViewById(R.id.et_input_iphone);
        mEtInputVerifyCode = findViewById(R.id.et_input_code);
        mBtSendVerifyCode = findViewById(R.id.bt_send_verify_code);
        mBtNextStep = findViewById(R.id.bt_next_step);
    }

    private void handlerData(){
        mIvBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Login
                ARouter.getInstance().build(PathConfig.Account.LOGIN).navigation();
            }
        });

        mBtSendVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO send code
            }
        });

        mBtNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputPhone = mEtInputPhone.getText().toString();
                String verifyCode = mEtInputVerifyCode.getText().toString();
                if (!TextUtils.isEmpty(inputPhone)){
                    ToastUtil.showShort(mContext,"输入的手机号为空！");
                }

                if (!ValidatorUtil.isMobile(inputPhone)){
                    ToastUtil.showShort(mContext,"输入的手机号格式不对，请重新输入！");
                }

                if (TextUtils.isEmpty(verifyCode)){
                    ToastUtil.showShort(mContext,"输入的验证码为空，请重新输入！");
                }
                ThreadPoolUtil.diskExe(()->{
                    UserInfo userInfo = AppDataBase.getInstance().userDao().selectByTelephone(inputPhone);
                    if (userInfo!= null){
                        if (userInfo.getTelephone().equals(inputPhone)){
                            runOnUiThread(()->{
                                ARouter.getInstance().build(PathConfig.Account.RESET_PASSWORD)
                                        .withString(ResetPasswordActivity.RESET_TELEPHONE,inputPhone)
                                        .navigation();
                            });
                        }
                    }
                });
            }
        });
    }

}