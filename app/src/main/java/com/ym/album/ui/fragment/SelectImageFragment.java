package com.ym.album.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ym.album.R;
import com.ym.album.app.config.AppConstant;
import com.ym.album.app.config.PathConfig;
import com.ym.album.base.BaseFragment;
import com.ym.album.event.Event;
import com.ym.album.event.EventBusUtil;
import com.ym.album.ui.activity.Constant;
import com.ym.album.ui.adapter.ImageRecyclerAdapter;
import com.ym.album.ui.bean.AlbumBean;
import com.ym.album.utils.ImageMediaUtil;
import com.ym.common_util.utils.LogUtil;
import com.ym.common_util.utils.SpUtil;
import com.ym.common_util.utils.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route(path = PathConfig.Image.SELECT_IMAGE_FRAGMENT)
public class SelectImageFragment extends BaseFragment {
    private static final String TAG = "SelectImageFragment";
    private RecyclerView imageRecyclerView;
    private ImageRecyclerAdapter imageRecyclerAdapter;
    private ImageView mIvLoadingRefresh;
    private TextView mTvLoadingRefresh;
    private List<String> allImageList;

    public SelectImageFragment() {
        // Required empty public constructor
    }

    public static SelectImageFragment newInstance() {
        return new SelectImageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_select_image, container, true);
        imageRecyclerView = view.findViewById(R.id.rv_img);
        mIvLoadingRefresh = view.findViewById(R.id.iv_select_image_loading_refresh);
        mTvLoadingRefresh = view.findViewById(R.id.tv_select_image_loading_refresh);
        // 是否第一次登录
        boolean isFirstLogin = SpUtil.getInstance(getContext()).getBoolean(Constant.Account.IS_FIRST_LOGIN,false);

        String allImageJson = SpUtil.getInstance(getContext()).getString(AppConstant.LAST_LOADING_ALL_IMAGE,"");
        long lastLoadingTime = SpUtil.getInstance(getContext()).getLong(AppConstant.LAST_LOADING_ALL_IMAGE_TIME,0L);
        if (!isFirstLogin) {
            if (!TextUtils.isEmpty(allImageJson) &&
                    (System.currentTimeMillis() - lastLoadingTime < AppConstant.LOADING_IMAGE_ONE_DAY)) {
                allImageList = new Gson().fromJson(allImageJson, new TypeToken<List<String>>() {}.getType());
                if (allImageList != null && allImageList.size() > 0) {
                    setImageRecyclerView();
                }
            } else {
                loadingRefreshAnim(true);
                ThreadPoolUtil.diskExe(() -> {
                    ImageMediaUtil.getImagePathList(requireContext());
                });
            }
        }else {
            allImageList = ImageMediaUtil.getImagePathList(requireContext());
            setImageRecyclerView();
        }
        return view;
    }

    public void setImageRecyclerView(){
        if (allImageList!=null && allImageList.size()>0) {
            imageRecyclerAdapter = new ImageRecyclerAdapter(getContext(), allImageList);

            imageRecyclerView.setAdapter(imageRecyclerAdapter);

            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            imageRecyclerView.setLayoutManager(layoutManager);

            imageRecyclerView.scrollToPosition(0);
            imageRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_fall_down));
            imageRecyclerView.scheduleLayoutAnimation();
        }else {
            loadingRefreshAnim(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        loadingRefreshAnim(false);
        LogUtil.d(TAG,"onPause()");
    }

    @Override
    public void onResume() {
        super.onResume();
        loadingRefreshAnim(true);
        LogUtil.d(TAG,"onResume()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG,"onDestroy()");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_image;
    }

    @Override
    public void initView() {

    }

    @Override
    public void onMessageEvent(Event event) {
        if (event.getCode()==AppConstant.LOADING_ALL_IMAGE_EVENT){
            allImageList = (List<String>) event.getData();
            loadingRefreshAnim(false);
            setImageRecyclerView();
        }
        super.onMessageEvent(event);
    }

    public void loadingRefreshAnim(boolean isStart){
        if (isStart){
            if (allImageList == null || allImageList.size()<1) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_loading_refresh);
                animation.setRepeatCount(Animation.INFINITE);
                animation.setBackgroundColor(getResources().getColor(R.color.transparent));
                animation.setInterpolator(new LinearInterpolator());
                mIvLoadingRefresh.setVisibility(View.VISIBLE);
                mTvLoadingRefresh.setVisibility(View.VISIBLE);
                mIvLoadingRefresh.startAnimation(animation);
                LogUtil.d(TAG,"loadingRefreshAnim(): start loading anim!");
            }
        }else {
            if (allImageList!=null && allImageList.size()>0) {
                mIvLoadingRefresh.setVisibility(View.GONE);
                mTvLoadingRefresh.setVisibility(View.GONE);
                mIvLoadingRefresh.clearAnimation();
                LogUtil.d(TAG,"loadingRefreshAnim(): stop loading anim!");
            }
        }
    }
}