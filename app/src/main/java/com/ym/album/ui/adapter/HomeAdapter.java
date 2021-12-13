package com.ym.album.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class HomeAdapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList;

    public HomeAdapter(@NonNull FragmentActivity fragmentActivity,List<Fragment> fragmentList) {
        super(fragmentActivity);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return this.fragmentList==null?null:this.fragmentList.get(position);
    }
    @Override
    public int getItemCount() {
        return this.fragmentList==null?0:fragmentList.size();
    }
}
