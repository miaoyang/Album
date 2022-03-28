package com.ym.album.ui.activity.account;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ym.album.AlbumApp;
import com.ym.album.app.config.AppConstant;
import com.ym.album.app.config.PathConfig;
import com.ym.album.app.config.SPConfig;
import com.ym.album.base.BaseMvpActivity;
import com.ym.album.event.Event;
import com.ym.album.event.EventBusUtil;
import com.ym.album.room.AppDataBase;
import com.ym.album.room.model.UserInfo;
import com.ym.album.R;
import com.ym.album.ui.activity.Constant;
import com.ym.album.utils.ImageMediaUtil;
import com.ym.common_util.utils.LogUtil;
import com.ym.common_util.utils.SpUtil;
import com.ym.common_util.utils.ThreadPoolUtil;
import com.ym.common_util.utils.ToastUtil;
import com.ym.common_util.utils.ValidatorUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.Date;
@Route(path = PathConfig.Account.LOGIN)
public class LoginActivity extends BaseMvpActivity<AccountPresenter> implements AccountContract.View {
    private static final String TAG = "LoginActivity";

    // register parameters
    @Autowired(name = "telephone")
    String telephone;
    @Autowired(name = "password")
    String password;

    private EditText mEtUserName;
    private EditText mEtPassword;
    private CheckBox mCbRememberPassword;
    private Button mBtLogin;
    private Button mBtRegister;
    private Button mBtForgetPassword; // forget password

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG,"onCreate");
        setContentView(R.layout.activity_login);
        mContext = this;
        // 自动登录
        boolean isFirstLogin = SpUtil.getInstance(this).getBoolean(Constant.Account.IS_FIRST_LOGIN,false);
        long lastLoginTime =SpUtil.getInstance(AlbumApp.getApp()).getLong(Constant.Account.AUTO_LOGIN_TIME,0L);
        if(System.currentTimeMillis()-lastLoginTime<Constant.Account.AUTO_LOGIN_MAX_TIME && !isFirstLogin){
            ARouter.getInstance().build(PathConfig.HOME.MAIN_ACTIVITY).navigation();
            EventBusUtil.sendStickyEvent(new Event<String>(AppConstant.LOGIN_SUCCESS_NOT_LOADING_IMAGE,"login success, not loading image!"));
        }

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

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(){
        mEtUserName = findViewById(R.id.et_username);
        mEtPassword = findViewById(R.id.et_password);
        mCbRememberPassword = findViewById(R.id.cb_remember_password);
        mBtLogin = findViewById(R.id.bt_login);
        mBtRegister = findViewById(R.id.bt_register);
        mBtForgetPassword = findViewById(R.id.bt_forget_password);

        mPresenter = new AccountPresenter();
        mPresenter.attachView(this);

    }
    @Override
    public void initData(){
        // TODO delete
        Date date = new Date(System.currentTimeMillis());
        UserInfo userInfoTemp = new UserInfo("yangmiao","123456","13340244000",
                "864412543@qq.com",26,date);
        ThreadPoolUtil.diskExe(()->{
            AppDataBase.getInstance().userDao().insertUserInfo(userInfoTemp);
            LogUtil.d(TAG,"initData(): insert data success!");
        });

        boolean isRememberPassword = SpUtil.getInstance(this).getBoolean(SPConfig.KEY_REMEMBER_PASSWORD,false);
        if (isRememberPassword){
            String spUserName = SpUtil.getInstance(this).getString(SPConfig.KEY_USER_NAME,"");
            String spPassword = SpUtil.getInstance(this).getString(SPConfig.KEY_PASSWORD,"");
            LogUtil.d(TAG,"onClick(): userName="+spUserName+" psd="+spPassword);
            if (!TextUtils.isEmpty(spUserName) && !TextUtils.isEmpty(spPassword)){
                mEtUserName.setText(spUserName);
                mEtPassword.setText(spPassword);
            }
            mCbRememberPassword.setChecked(true);
        }

//        @SuppressLint("ResourceType")
//        Animator loginAnimator = AnimatorInflater.loadAnimator(mContext,R.anim.anim_rotate_login);
//        loginAnimator.setTarget(btLogin);
//        loginAnimator.start();

        mCbRememberPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCbRememberPassword.isChecked()){
                    SpUtil.getInstance(mContext).putBoolean(SPConfig.KEY_REMEMBER_PASSWORD,true);
                }
            }
        });
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEtUserName != null && mEtPassword != null){
                    String userName = mEtUserName.getText().toString();
                    String password = mEtPassword.getText().toString();
                    LogUtil.d(TAG,"initData(): userName="+userName+" password="+password);
                    // TODO 区分手机号、邮箱、普通账号
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
                                LogUtil.d(TAG,"initData(): phone login success! userName="+userName);
                                ARouter.getInstance().build(PathConfig.HOME.MAIN_ACTIVITY).navigation();
                                if (mCbRememberPassword.isChecked()){
                                    SpUtil.getInstance(mContext).putString(SPConfig.KEY_USER_NAME,userName);
                                    SpUtil.getInstance(mContext).putString(SPConfig.KEY_PASSWORD,password);
                                    // 存在bug，多个账号切换问题
                                    LogUtil.d(TAG,"initData(): phone save username and password in sp!");
                                }else {
                                    SpUtil.getInstance(mContext).putString(SPConfig.KEY_USER_NAME,"");
                                    SpUtil.getInstance(mContext).putString(SPConfig.KEY_PASSWORD,"");
                                }
                                SpUtil.getInstance(mContext).putLong(Constant.Account.AUTO_LOGIN_TIME,System.currentTimeMillis());
                                EventBusUtil.sendStickyEvent(new Event<String>(AppConstant.LOGIN_SUCCESS_LOADING_IMAGE,"login success"));
                                LogUtil.d(TAG,"initData(): sendEvent login success");
                            }else {
                                runOnUiThread(()->{
                                    mEtPassword.setText("");
                                    ToastUtil.showShort(AlbumApp.getApp(),"输入密码错误，请重新输入！");
                                });
                                LogUtil.d(TAG,"initData(): phone password is error userName="+userName);
                            }
                        }else if (userInfoTel != null){
                            if (userInfoTel.getPassword().equals(password)){
                                LogUtil.d(TAG,"initData(): tel login success! userName="+userName);
                                ARouter.getInstance().build(PathConfig.HOME.MAIN_ACTIVITY).navigation();
                                if (mCbRememberPassword.isChecked()){
                                    SpUtil.getInstance(mContext).putString(SPConfig.KEY_USER_NAME,"");
                                    SpUtil.getInstance(mContext).putString(SPConfig.KEY_PASSWORD,"");
                                    // 存在bug，多个账号切换问题
                                    LogUtil.d(TAG,"initData(): tel save username and password in sp!");
                                }
                                // 记录登录时间
                                SpUtil.getInstance(mContext).putLong(Constant.Account.AUTO_LOGIN_TIME,System.currentTimeMillis());
                                SpUtil.getInstance(mContext).putBoolean(Constant.Account.IS_FIRST_LOGIN,true);
                                EventBusUtil.sendStickyEvent(new Event<String>(AppConstant.LOGIN_SUCCESS_LOADING_IMAGE,"login success"));
                            }else {
                                runOnUiThread(()->{
                                    mEtPassword.setText("");
                                    ToastUtil.showShort(AlbumApp.getApp(),"输入密码错误，请重新输入！");
                                });
                                LogUtil.d(TAG,"initData(): tel password is error userName="+userName);
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

        mBtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(PathConfig.Account.REGISTER).navigation();
            }
        });

        mBtForgetPassword.setOnClickListener((view)->{
            ARouter.getInstance().build(PathConfig.Account.FORGET_PASSWORD).navigation();
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(String errorMsg) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(Event event){
        if (event.getCode()== AppConstant.LOGIN_SUCCESS_LOADING_IMAGE){
            String str = (String) event.getData();
            LogUtil.d(TAG,"onMessageEvent(): str="+str);
            ThreadPoolUtil.diskExe(new Runnable() {
                @Override
                public void run() {
                    ImageMediaUtil.getAlbumList(mContext,getParent());
                }
            });
            ThreadPoolUtil.diskExe(new Runnable() {
                @Override
                public void run() {
                    ImageMediaUtil.getImagePathList(mContext,getParent());
                }
            });
        }else if (event.getCode()==AppConstant.LOGIN_SUCCESS_NOT_LOADING_IMAGE){
            String str = (String) event.getData();
            LogUtil.d(TAG,"onMessageEvent(): str="+str);
        }
    }
}
