package com.ym.album.ui.activity.account;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.ym.album.AlbumApp;
import com.ym.album.app.config.PathConfig;
import com.ym.album.app.config.SPConfig;
import com.ym.album.room.AppDataBase;
import com.ym.album.room.model.UserInfo;
import com.ym.album.ui.BaseActivity;
import com.ym.album.R;
import com.ym.common.utils.LogUtil;
import com.ym.common.utils.SpUtil;
import com.ym.common.utils.ThreadPoolUtil;
import com.ym.common.utils.ToastUtil;
import com.ym.common.utils.ValidatorUtil;

import java.sql.Date;
@Route(path = PathConfig.Account.LOGIN)
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    // register parameters
    @Autowired(name = "telephone")
    String telephone;
    @Autowired(name = "password")
    String password;

    private EditText etUserName;
    private EditText etPassword;
    private CheckBox cbRememberPassword;
    private Button btLogin;
    private Button btRegister;
    private Button mBtForgetPassword; // forget password

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG,"onCreate");
        setContentView(R.layout.activity_login);
        mContext = this;
        initView();
        initData();
        LogUtil.d(TAG,"register telephone="+telephone+" password="+password);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG,"onDestroy(): ");
    }

    private void initView(){
        etUserName = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        cbRememberPassword = findViewById(R.id.cb_remember_password);
        btLogin = findViewById(R.id.bt_login);
        btRegister = findViewById(R.id.bt_register);
        mBtForgetPassword = findViewById(R.id.bt_forget_password);
    }

    public void initData(){
        // TODO delete
        Date date = new Date(System.currentTimeMillis());
        UserInfo userInfoTemp = new UserInfo("yangmiao","123456","13340244009",
                "864412543@qq.com",26,date);
        ThreadPoolUtil.diskExe(()->{
            AppDataBase.getInstance().userDao().insertUserInfo(userInfoTemp);
            LogUtil.d(TAG,"insert data success!");
        });

        String spUserName = SpUtil.getInstance(AlbumApp.getApp()).getString(SPConfig.KEY_USER_NAME,"");
        String spPassword = SpUtil.getInstance(AlbumApp.getApp()).getString(SPConfig.KEY_PASSWORD,"");
        LogUtil.d(TAG,"onClick(): userName="+spUserName+" psd="+spPassword);
        if (!TextUtils.isEmpty(spUserName) && !TextUtils.isEmpty(spPassword)){
                etUserName.setText(spUserName);
                etPassword.setText(spPassword);
        }

        if (!TextUtils.isEmpty(telephone)){
            runOnUiThread(()->{
                etUserName.setText(telephone);
                LogUtil.d(TAG,"register username and password setText success!");
            });
        }
        if (!TextUtils.isEmpty(password)){
            etPassword.setText(password);
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUserName != null && etPassword != null){
                    String userName = etUserName.getText().toString();
                    String password = etPassword.getText().toString();
                    LogUtil.d(TAG,"userName="+userName+" password="+password);
                    if (TextUtils.isEmpty(userName)){
                        ToastUtil.showShort(mContext,"输入用户名为空！");
                        return;
                    }
                    if (TextUtils.isEmpty(password)){
                        ToastUtil.showShort(mContext,"输入密码为空！");
                        return;
                    }
                    if (userName.length()>26 || password.length()>26){
                        ToastUtil.showShort(mContext,"用户名或密码太长，请重新输入！");
                        return;
                    }
//                    if (!ValidatorUtil.isUserName(userName)){
//                        ToastUtil.showShort(mContext,"输入用户名格式不对，请重新输入！");
//                        return;
//                    }
                    if (!ValidatorUtil.isPassword(password)){
                        ToastUtil.showShort(mContext,"输入密码格式不对，请重新输入！");
                        return;
                    }

                    // read from room
                    ThreadPoolUtil.diskExe(()->{
                        UserInfo userInfoName = AppDataBase.getInstance().userDao().selectByUsername(userName);
                        UserInfo userInfoTel = AppDataBase.getInstance().userDao().selectByTelephone(userName);
                        if (userInfoName != null ){
                            if (userInfoName.getPassword().equals(password)){
                                LogUtil.d(TAG,"phone login success! userName="+userName);
                                ARouter.getInstance().build(PathConfig.HOME.MAIN_ACTIVITY).navigation();
                                if (cbRememberPassword.isChecked()){
                                    SpUtil.getInstance(AlbumApp.getApp()).putString(SPConfig.KEY_USER_NAME,"");
                                    SpUtil.getInstance(AlbumApp.getApp()).putString(SPConfig.KEY_PASSWORD,"");
                                    // 存在bug，多个账号切换问题
                                    LogUtil.d(TAG,"phone save username and password in sp!");
                                }
                            }else {
                                runOnUiThread(()->{
                                    etPassword.setText("");
                                    ToastUtil.showShort(AlbumApp.getApp(),"输入密码错误，请重新输入！");
                                });
                                LogUtil.d(TAG,"phone password is error userName="+userName);
                            }
                        }else if (userInfoTel != null){
                            if (userInfoTel.getPassword().equals(password)){
                                LogUtil.d(TAG,"tel login success! userName="+userName);
                                ARouter.getInstance().build(PathConfig.HOME.MAIN_ACTIVITY).navigation();
                                if (cbRememberPassword.isChecked()){
                                    SpUtil.getInstance(AlbumApp.getApp()).putString(SPConfig.KEY_USER_NAME,"");
                                    SpUtil.getInstance(AlbumApp.getApp()).putString(SPConfig.KEY_PASSWORD,"");
                                    // 存在bug，多个账号切换问题
                                    LogUtil.d(TAG,"tel save username and password in sp!");
                                }
                            }else {
                                runOnUiThread(()->{
                                    etPassword.setText("");
                                    ToastUtil.showShort(AlbumApp.getApp(),"输入密码错误，请重新输入！");
                                });
                                LogUtil.d(TAG,"tel password is error userName="+userName);
                            }
                        }else {
                            runOnUiThread(()->{
                                ToastUtil.showShort(AlbumApp.getApp(),"未注册账号，请注册账号！");
                            });
                            ARouter.getInstance().build(PathConfig.Account.REGISTER).navigation();
                        }
                    });
                }
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(PathConfig.Account.REGISTER).navigation();
            }
        });

        mBtForgetPassword.setOnClickListener((view)->{
            ARouter.getInstance().build(PathConfig.Account.FORGET_PASSWORD).navigation();
        });
    }
}
