package com.ym.album.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ym.album.AlbumApp;
import com.ym.album.R;
import com.ym.album.app.config.AppConstant;
import com.ym.album.base.BaseFragment;
import com.ym.album.event.Event;
import com.ym.album.ui.adapter.AlbumRecyclerAdapter;
import com.ym.album.ui.bean.AlbumBean;
import com.ym.album.utils.ImageMediaUtil;
import com.ym.common_util.utils.JsonUtil;
import com.ym.common_util.utils.LogUtil;
import com.ym.common_util.utils.SpUtil;
import com.ym.common_util.utils.ThreadPoolUtil;

import java.util.ArrayList;


public class AlbumFragment extends BaseFragment {
    private static final String TAG = "AlbumFragment";

    private RecyclerView recyclerView;
    private AlbumRecyclerAdapter albumRecyclerAdapter;
    private ImageView mIvLoadingRefresh;
    private TextView mTvLoadingRefresh;
    private View v;

    ArrayList<AlbumBean> albumAllList;

    public AlbumFragment() {

    }

    public static AlbumFragment newInstance() {
        AlbumFragment fragment = new AlbumFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, true);
        this.v = view;
        recyclerView = view.findViewById(R.id.rv_album);
        mIvLoadingRefresh = view.findViewById(R.id.iv_loading_refresh);
        mTvLoadingRefresh = view.findViewById(R.id.tv_loading_refresh);

        String albumJson = SpUtil.getInstance(getContext()).getString(AppConstant.ALBUM_LIST_KEY,"");
        long lastLoadingTime = SpUtil.getInstance(getContext()).getLong(AppConstant.LAST_LOADING_IMAGE,0L);
        boolean isLessThanOneDay = System.currentTimeMillis()-lastLoadingTime<AppConstant.LOADING_IMAGE_ONE_DAY;
        LogUtil.d(TAG,"onCreateView(): isLessThanOneDay="+isLessThanOneDay+" albumJson="+albumJson);
        if (!TextUtils.isEmpty(albumJson) && isLessThanOneDay){
            albumAllList = new Gson().fromJson(albumJson,new TypeToken<ArrayList<AlbumBean>>(){}.getType());
            setRecyclerView();
        }else {
            loadingRefreshAnim(true);
            ThreadPoolUtil.diskExe(()->{
                ImageMediaUtil.getAlbumList(getContext(),getActivity());
            });
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.d(TAG,"onHiddenChanged(): hidden="+hidden);

    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d(TAG,"onPause(): album");
        loadingRefreshAnim(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(TAG,"onResume(): album");
        loadingRefreshAnim(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_album;
    }

    @Override
    public void initView() {

    }

    @Override
    public void onMessageEvent(Event event){
        if (event.getCode()==AppConstant.ALBUM_EVENT_1){
            albumAllList = (ArrayList<AlbumBean>) event.getData();
            LogUtil.d(TAG,"onMessageEvent(): "+event.getCode()+" albumAllList size="+albumAllList.size());
            loadingRefreshAnim(false);
            setRecyclerView();
        }
    }

    /**
     * 加载和结束动画
     * @param isStart
     */
    public void loadingRefreshAnim(boolean isStart){
        if (isStart){
            if (albumAllList == null || albumAllList.size()<1) {
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
            if (albumAllList!=null && albumAllList.size()>0) {
                mIvLoadingRefresh.setVisibility(View.GONE);
                mTvLoadingRefresh.setVisibility(View.GONE);
                mIvLoadingRefresh.clearAnimation();
                LogUtil.d(TAG,"loadingRefreshAnim(): stop loading anim!");
            }
        }
    }

    public void setRecyclerView(){
        if (albumAllList!=null && albumAllList.size()>0) {
            albumRecyclerAdapter = new AlbumRecyclerAdapter(getContext(),albumAllList );
            albumRecyclerAdapter.setData(albumAllList);

            LogUtil.d(TAG,"onMessageEvent(): albumRecyclerAdapter="+albumRecyclerAdapter);
            recyclerView.setAdapter(albumRecyclerAdapter);

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);

            recyclerView.scrollToPosition(0);
            recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_fall_down));
            recyclerView.scheduleLayoutAnimation();
        }else {
            loadingRefreshAnim(true);
        }
    }
}