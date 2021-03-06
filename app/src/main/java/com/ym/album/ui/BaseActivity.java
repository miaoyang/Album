package com.ym.album.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;
import com.ym.album.R;
import com.ym.album.app.config.ARouterConfig;
import com.ym.album.app.config.StyleConfig;
import com.ym.album.event.EventBusUtil;
import com.ym.common.config.DebugConfig;
import com.ym.common.utils.CrashHandlerUtil;

public class BaseActivity extends AppCompatActivity {
    private final int mRequestCode = 1024;
    private RequestPermissionCallBack mRequestPermissionCallBack;

    private void initConfig(){
        DebugConfig.setDebug(true);
        DebugConfig.setAppTag("Album");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashHandlerUtil.getInstance().init(this);
        initConfig();
        ARouter.getInstance().inject(this);
//        if (StyleConfig.isSetStatusBar){
//            steepStatusBar();
//        }
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .statusBarDarkFont(true)
                .fitsSystemWindows(false)
                .init();
        EventBusUtil.register(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllGranted = true;
        StringBuilder sb = new StringBuilder();
        for (String s:permissions){
            sb = sb.append(s+"\r\n");
        }
        switch (requestCode){
            case mRequestCode:{
                for(int i = 0;i<grantResults.length;i++){
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                        hasAllGranted = false;
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[i])){
                            new AlertDialog.Builder(this)
                                    .setTitle("Permission")
                                    .setMessage("???????????????????????????????????????????????????????????????????????????MIUI??????\" +\n" +
                                            "?????????????????????????????????????????????,?????????????????????????????????????????????\" +\n" +
                                            "????????????????????????:\" + permissionName +\n" +
                                            "???????????????????????????????????????????????????????????????????????????\"")
                                    .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package",getApplicationContext().getPackageName(),null);
                                            intent.setData(uri);
                                            startActivity(intent);
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialogInterface) {
                                            mRequestPermissionCallBack.denied();
                                        }
                                    })
                                    .show();
                        }else {
                            mRequestPermissionCallBack.denied();
                        }
                        break;
                    }
                }
                if (hasAllGranted){
                    mRequestPermissionCallBack.granted();
                }
            }
        }
    }

    public void requestPermissions(final Context context, final String[] permissions,
                                   RequestPermissionCallBack callback) {
        this.mRequestPermissionCallBack = callback;
        StringBuilder permissionNames = new StringBuilder();
        for (String s : permissions) {
            permissionNames = permissionNames.append(s + "\r\n");
        }
        //????????????????????????????????????????????????????????????,????????????????????????????????????????????????
        boolean isAllGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                isAllGranted = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    new AlertDialog.Builder(BaseActivity.this).setTitle("PermissionTest")//?????????????????????
                            .setMessage("?????????????????????????????????????????????????????????????????????????????????" +
                                    "??????????????????????????????" + permissionNames +
                                    " ?????????????????????????????????????????????????????????")//?????????????????????
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {//??????????????????
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//???????????????????????????
                                    //TODO Auto-generated method stub
                                    ActivityCompat.requestPermissions(((Activity) context), permissions, mRequestCode);
                                }
                            }).show();//??????????????????????????????????????????
                } else {
                    ActivityCompat.requestPermissions(((Activity) context), permissions, mRequestCode);
                }
                break;
            }
        }
        if (isAllGranted) {
            mRequestPermissionCallBack.granted();
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    public interface RequestPermissionCallBack {
        void granted();
        void denied();
    }

    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // ???????????????
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // ???????????????
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

    }

    protected <T extends View> T f(int resId) {
        return (T) super.findViewById(resId);
    }

}