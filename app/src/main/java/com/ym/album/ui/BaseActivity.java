package com.ym.album.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class BaseActivity extends AppCompatActivity {
    private final int mRequestCode = 1024;
    private RequestPermissionCallBack mRequestPermissionCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                                    .setMessage("【用户选择了不再提示按钮，或者系统默认不在提示（如MIUI）。\" +\n" +
                                            "引导用户到应用设置页去手动授权,注意提示用户具体需要哪些权限】\" +\n" +
                                            "获取相关权限失败:\" + permissionName +\n" +
                                            "将导致部分功能无法正常使用，需要到设置页面手动授权\"")
                                    .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package",getApplicationContext().getPackageName(),null);
                                            intent.setData(uri);
                                            startActivity(intent);
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
        //如果所有权限都已授权，则直接返回授权成功,只要有一项未授权，则发起权限请求
        boolean isAllGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                isAllGranted = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    new AlertDialog.Builder(BaseActivity.this).setTitle("PermissionTest")//设置对话框标题
                            .setMessage("【用户曾经拒绝过你的请求，所以这次发起请求时解释一下】" +
                                    "您好，需要如下权限：" + permissionNames +
                                    " 请允许，否则将影响部分功能的正常使用。")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    //TODO Auto-generated method stub
                                    ActivityCompat.requestPermissions(((Activity) context), permissions, mRequestCode);
                                }
                            }).show();//在按键响应事件中显示此对话框
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


    public interface RequestPermissionCallBack {
        void granted();
        void denied();
    }

}