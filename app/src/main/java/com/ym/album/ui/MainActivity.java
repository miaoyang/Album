package com.ym.album.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ym.album.R;
import com.ym.album.databinding.ActivityMainBinding;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;

    private List<Fragment> mFragmentsList;
    private FragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(mPageChangeListener);
    }

    private void initView(){
        mViewPager = findViewById(R.id.fragment_vp);
        mRadioGroup = findViewById(R.id.tabs_rg);

        mFragmentsList = new ArrayList<>(4);
        mFragmentsList.add(BlankFragment.newInstance("图库"));
        mFragmentsList.add(BlankFragment.newInstance("为您推荐"));
        mFragmentsList.add(BlankFragment.newInstance("相薄"));
        mFragmentsList.add(BlankFragment.newInstance("搜索"));

        mFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),mFragmentsList);
        mViewPager.setAdapter(mFragmentPagerAdapter);

        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
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

    private class MainFragmentPagerAdapter extends FragmentPagerAdapter{
        private List<Fragment> list;

        public MainFragmentPagerAdapter(@NonNull FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return this.list==null?null:this.list.get(position);
        }

        @Override
        public int getCount() {
            return this.list==null?0:list.size();
        }
    }

}