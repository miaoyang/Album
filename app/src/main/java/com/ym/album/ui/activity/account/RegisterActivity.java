package com.ym.album.ui.activity.account;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ym.album.AlbumApp;
import com.ym.album.R;
import com.ym.album.app.config.PathConfig;
import com.ym.album.room.AppDataBase;
import com.ym.album.room.model.UserInfo;
import com.ym.album.ui.BaseActivity;
import com.ym.album.ui.activity.Constant;
import com.ym.common.utils.LogUtil;
import com.ym.common.utils.ThreadPoolUtil;
import com.ym.common.utils.ToastUtil;
import com.ym.common.utils.ValidatorUtil;

import android.content.Context;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Date;

@Route(path = PathConfig.Account.REGISTER)
public class RegisterActivity extends BaseActivity {
    private static final String TAG ="RegisterActivity";

    private EditText mEtInputPhoneNum;
    private Button mBtInputCode;
    private EditText mEtInputCode;
    private EditText mEtInputPsd;
    private EditText mEtInputPsdAgain;
    private Button mBtRegister;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        initView();
        handlerData();
    }

    private void initView(){
        mEtInputPhoneNum = findViewById(R.id.et_input_iphone_number);
        mBtInputCode = findViewById(R.id.bt_send_code);
        mEtInputCode = findViewById(R.id.et_input_code);
        mEtInputPsd = findViewById(R.id.et_input_password);
        mEtInputPsdAgain = findViewById(R.id.et_input_password_again);
        mBtRegister = findViewById(R.id.bt_register_account);
    }

    private void handlerData(){

        mBtInputCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });

        mBtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNum = mEtInputPhoneNum.getText().toString();
                String inputCode = mEtInputCode.getText().toString();
                String psd = mEtInputPsd.getText().toString();
                String psdAgain = mEtInputPsdAgain.getText().toString();

                if (TextUtils.isEmpty(phoneNum)){
                    ToastUtil.showShort(mContext,"????????????????????????!");
                    return;
                }
                if (!ValidatorUtil.isMobile(phoneNum)){
                    ToastUtil.showShort(mContext,"??????????????????????????????!");
                    return;
                }
                ThreadPoolUtil.diskExe(()->{
                    UserInfo userInfo = AppDataBase.getInstance().userDao().selectByTelephone(phoneNum);
                    if (userInfo != null){
                        if (userInfo.getTelephone().equals(phoneNum)){
                            runOnUiThread(()->{
                                ToastUtil.showShort(mContext,"??????????????????????????????????????????");
                            });
                            ARouter.getInstance().build(PathConfig.Account.LOGIN)
                                    .withString(Constant.Account.telephone,phoneNum)
                                    .navigation();
                            LogUtil.d(TAG,"ARouter: telephone "+phoneNum);
                        }
                    }
                });

                if (TextUtils.isEmpty(inputCode)){
                    ToastUtil.showShort(mContext,"????????????????????????!");
                    return;
                }
                if (TextUtils.isEmpty(psd)){
                    ToastUtil.showShort(mContext,"????????????????????????");
                    return;
                }
                if(!ValidatorUtil.isPassword(psd)){
                    ToastUtil.showShort(mContext,"?????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(psdAgain)){
                    ToastUtil.showShort(mContext,"????????????????????????");
                    return;
                }
                if(!ValidatorUtil.isPassword(psdAgain)){
                    ToastUtil.showShort(mContext,"???????????????????????????????????????");
                    return;
                }
                if (!psd.equals(psdAgain)){
                    ToastUtil.showShort(mContext,"???????????????????????????????????????????????????");
                    return;
                }
                // TODO request server
                // 1.insert userInfo in file
                UserInfo userInfo = new UserInfo("",psd,phoneNum,"",0,
                        new Date(System.currentTimeMillis()));
                ThreadPoolUtil.diskExe(()->{
                    AppDataBase.getInstance().userDao().insertUserInfo(userInfo);
                    LogUtil.d(TAG,"save register info in file!");
                });

                ARouter.getInstance().build(PathConfig.Account.LOGIN)
                        .withString(Constant.Account.telephone,phoneNum)
                        .withString(Constant.Account.password,psd)
                        .navigation();
            }
        });


    }

}