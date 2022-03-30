package com.ym.album.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ym.album.event.Event;
import com.ym.album.event.EventBusUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/2/28 20:11
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtil.register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(),container,false);
        initView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    public abstract int getLayoutId();

    public abstract void initView();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event){

    }

    /**
     * 页面跳转，从当前页面跳转到指定fragment
     * @param nowLayoutId
     * @param f
     */
    public void switchFragment(int nowLayoutId,Fragment f) {
        if (getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();//管理对象
            FragmentTransaction trans = fragmentManager.beginTransaction();//切换碎片
            trans.replace(nowLayoutId, f);
            trans.commit();
        }
    }

    /**
     * 添加fragment
     * @param nowLayoutId
     * @param f
     */
    public void addFragment(int nowLayoutId,Fragment f){
        if (getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();//管理对象
            FragmentTransaction trans = fragmentManager.beginTransaction();//切换碎片
            trans.add(nowLayoutId, f);
            trans.commit();
        }
    }
}
