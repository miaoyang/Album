package com.ym.album.ui.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ym.album.AlbumApp;
import com.ym.album.R;
import com.ym.album.app.config.PathConfig;
import com.ym.album.base.BaseActivity;
import com.ym.album.ui.adapter.HomeAdapter;
import com.ym.album.ui.fragment.AlbumFragment;
import com.ym.album.ui.fragment.BlankFragment;
import com.ym.album.ui.fragment.person.PersonalFragment;
import com.ym.album.ui.fragment.SelectImageFragment;
import com.ym.album.ui.fragment.recommend.RecommendFragment;
import com.ym.common_util.utils.SpUtil;

import android.annotation.SuppressLint;
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
        mFragmentsList.add(PersonalFragment.newInstance());
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
     * fragment切换动画
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
                mTransaction.replace(R.id.rb_home_search,PersonalFragment.newInstance());
                fragmentNow=3;
                break;
        }
        mTransaction.commit();
    }

}