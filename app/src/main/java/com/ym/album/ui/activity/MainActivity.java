package com.ym.album.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ym.album.R;
import com.ym.album.app.config.PathConfig;
import com.ym.album.base.BaseActivity;
import com.ym.album.ui.adapter.HomeAdapter;
import com.ym.album.ui.fragment.AlbumFragment;
import com.ym.album.ui.fragment.person.PersonalHomeFragment;
import com.ym.album.ui.fragment.SelectImageFragment;
import com.ym.album.ui.fragment.recommend.RecommendFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

@Route(path = PathConfig.HOME.MAIN_ACTIVITY)
public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();



    private ViewPager2 mViewPager;
    private RadioGroup mRadioGroup;

    private List<Fragment> mFragmentsList;
    private HomeAdapter mHomeAdapter;

    private FragmentManager fragmentManager;
    private FragmentTransaction mTransaction;

    private int fragmentNow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        checkPermission(this,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.unregisterOnPageChangeCallback(mPageChangeListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initView(){
        mViewPager = findViewById(R.id.fragment_vp);
        mRadioGroup = findViewById(R.id.tabs_rg);

        mFragmentsList = new ArrayList<>(4);
        mFragmentsList.add(SelectImageFragment.newInstance());
        mFragmentsList.add(RecommendFragment.newInstance());
        mFragmentsList.add(AlbumFragment.newInstance());
        mFragmentsList.add(PersonalHomeFragment.newInstance());
        Log.d(TAG," list "+mFragmentsList);

        mHomeAdapter = new HomeAdapter(this, mFragmentsList);
        mRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mViewPager.setAdapter(mHomeAdapter);

        mViewPager.registerOnPageChangeCallback(mPageChangeListener);
        mViewPager.setCurrentItem(0);

        //changeFragment(fragmentNow);
    }

    @Override
    public void initData() {

    }

    private final ViewPager2.OnPageChangeCallback mPageChangeListener = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            switch (position){
                case 0:
                    ((RadioButton)findViewById(R.id.rb_home_album)).setChecked(true);
                    break;
                case 1:
                    ((RadioButton)findViewById(R.id.rb_home_recommend)).setChecked(true);
                    break;
                case 2:
                    ((RadioButton)findViewById(R.id.rb_home_relative)).setChecked(true);
                    break;
                case 3:
                    ((RadioButton)findViewById(R.id.rb_home_search)).setChecked(true);
                    break;
            }

        }
    };

    private final RadioGroup.OnCheckedChangeListener  mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };

    /**
     * fragment切换动画 // TODO
     * @param i
     */
    public void changeFragment(int i){
        fragmentManager=getSupportFragmentManager();
        mTransaction=fragmentManager.beginTransaction();
        /**
         * 判断动画的切换方向
         */
        if(fragmentNow>i){
            mTransaction.setCustomAnimations(R.anim.fragment_from_left,R.anim.fragment_out_left);
        }else if(fragmentNow<i){
            mTransaction.setCustomAnimations(R.anim.fragment_from_left,R.anim.fragment_out_right);
        }

        switch (i){
            case 0:
                mTransaction.replace(R.id.rb_home_album,SelectImageFragment.newInstance());
                fragmentNow=0;
                break;
            case 1:
                mTransaction.replace(R.id.rb_home_recommend,RecommendFragment.newInstance());
                fragmentNow=1;
                break;
            case 2:
                mTransaction.replace(R.id.rb_home_relative,AlbumFragment.newInstance());
                fragmentNow=2;
                break;
            case 3:
                mTransaction.replace(R.id.rb_home_search, PersonalHomeFragment.newInstance());
                fragmentNow=3;
                break;
        }
        mTransaction.commit();
    }

    /**
     * 申请权限
     * @param context
     * @param activity
     * @return
     */
    public static boolean checkPermission(Context context, Activity activity) {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_MEDIA_LOCATION
        };
        boolean isCheckPermissionTrue = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                isCheckPermissionTrue = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    new AlertDialog.Builder(context)
                            .setTitle("申请权限")
                            .setMessage("您将申请以下权限" + permission)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(activity, permissions, 0x01);
                                }
                            }).show();
                } else {
                    ActivityCompat.requestPermissions(activity, permissions, 0x01);
                }
                break;
            }
        }
        return isCheckPermissionTrue;
    }

}