package com.ym.album.ui;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.ym.album.R;
import com.ym.album.databinding.ActivityMainBinding;
import com.ym.album.ui.ui.adapter.HomeAdapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;

    private ViewPager2 mViewPager;
    private RadioGroup mRadioGroup;

    private List<Fragment> mFragmentsList;
    private HomeAdapter mHomeAdapter;

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

    private void initView(){
        mViewPager = findViewById(R.id.fragment_vp);
        mRadioGroup = findViewById(R.id.tabs_rg);

        mFragmentsList = new ArrayList<>(4);
        mFragmentsList.add(BlankFragment.newInstance("图库"));
        mFragmentsList.add(BlankFragment.newInstance("为您推荐"));
        mFragmentsList.add(BlankFragment.newInstance("相薄"));
        mFragmentsList.add(BlankFragment.newInstance("搜索"));
        Log.d(TAG," list "+mFragmentsList);

        mHomeAdapter = new HomeAdapter(this, mFragmentsList);
        mRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mViewPager.setAdapter(mHomeAdapter);

        mViewPager.registerOnPageChangeCallback(mPageChangeListener);
        mViewPager.setCurrentItem(0);
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

}