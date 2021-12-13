package com.ym.album.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;


import com.ym.album.action.ActivityAction;
import com.ym.album.action.BundleAction;
import com.ym.album.action.ClickAction;
import com.ym.album.action.HandlerAction;
import com.ym.album.action.KeyboardAction;
import com.ym.album.action.ResourcesAction;

import java.util.List;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : Fragment 技术基类
 */
public abstract class BaseFragment<A extends BaseActivity> extends Fragment implements
        ActivityAction, ResourcesAction, HandlerAction, ClickAction, BundleAction, KeyboardAction {
    private final int mRequestCode = 1024;
    private RequestPermissionCallBack mRequestPermissionCallBack;

    /** Activity 对象 */
    private A mActivity;
    /** 根布局 */
    private View mRootView;
    /** 当前是否加载过 */
    private boolean mLoading;

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 获得全局的 Activity
        mActivity = (A) requireActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutId() <= 0) {
            return null;
        }

        mLoading = false;
        mRootView = inflater.inflate(getLayoutId(), container, false);
        initView();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mLoading) {
            mLoading = true;
            initData();
            onFragmentResume(true);
            return;
        }

        if (mActivity != null && mActivity.getLifecycle().getCurrentState() == Lifecycle.State.STARTED) {
            onActivityResume();
        } else {
            onFragmentResume(false);
        }
    }

    /**
     * Fragment 可见回调
     *
     * @param first                 是否首次调用
     */
    protected void onFragmentResume(boolean first) {}

    /**
     * Activity 可见回调
     */
    protected void onActivityResume() {}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoading = false;
        removeCallbacks();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    /**
     * 这个 Fragment 是否已经加载过了
     */
    public boolean isLoading() {
        return mLoading;
    }

    @NonNull
    @Override
    public View getView() {
        return mRootView;
    }

    /**
     * 获取绑定的 Activity，防止出现 getActivity 为空
     */
    public A getAttachActivity() {
        return mActivity;
    }

    /**
     * 获取 Application 对象
     */
    public Application getApplication() {
        return mActivity != null ? mActivity.getApplication() : null;
    }

    /**
     * 获取布局 ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 根据资源 id 获取一个 View 对象
     */
    @Override
    public <V extends View> V findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    @Override
    public Bundle getBundle() {
        return getArguments();
    }

    /**
     * startActivityForResult 方法优化
     */

    public void startActivityForResult(Class<? extends Activity> clazz, BaseActivity.OnActivityCallback callback) {
        getAttachActivity().startActivityForResult(clazz, callback);
    }

    public void startActivityForResult(Intent intent, BaseActivity.OnActivityCallback callback) {
        getAttachActivity().startActivityForResult(intent, null, callback);
    }

    public void startActivityForResult(Intent intent, Bundle options, BaseActivity.OnActivityCallback callback) {
        getAttachActivity().startActivityForResult(intent, options, callback);
    }

    /**
     * 销毁当前 Fragment 所在的 Activity
     */
    public void finish() {
        if (mActivity == null || mActivity.isFinishing() || mActivity.isDestroyed()) {
            return;
        }
        mActivity.finish();
    }

    /**
     * Fragment 按键事件派发
     */
    public boolean dispatchKeyEvent(KeyEvent event) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            // 这个子 Fragment 必须是 BaseFragment 的子类，并且处于可见状态
            if (!(fragment instanceof BaseFragment) ||
                    fragment.getLifecycle().getCurrentState() != Lifecycle.State.RESUMED) {
                continue;
            }
            // 将按键事件派发给子 Fragment 进行处理
            if (((BaseFragment<?>) fragment).dispatchKeyEvent(event)) {
                // 如果子 Fragment 拦截了这个事件，那么就不交给父 Fragment 处理
                return true;
            }
        }
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                return onKeyDown(event.getKeyCode(), event);
            case KeyEvent.ACTION_UP:
                return onKeyUp(event.getKeyCode(), event);
            default:
                return false;
        }
    }

    /**
     * 按键按下事件回调
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 默认不拦截按键事件
        return false;
    }

    /**
     * 按键抬起事件回调
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // 默认不拦截按键事件
        return false;
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
                    new AlertDialog.Builder(getContext()).setTitle("PermissionTest")//设置对话框标题
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


    interface RequestPermissionCallBack {
        void granted();
        void denied();
    }
}