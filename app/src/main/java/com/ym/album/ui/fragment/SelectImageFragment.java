package com.ym.album.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.ym.album.R;
import com.ym.album.ui.adapter.ImageRecyclerAdapter;
import com.ym.album.utils.ImageMediaUtil;

import java.util.List;
import java.util.Objects;


public class SelectImageFragment extends Fragment {
    private static final String TAG = "SelectImageFragment";
    private RecyclerView imageRecyclerView;
    private ImageRecyclerAdapter imageRecyclerAdapter;

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

        List<String> imageList = ImageMediaUtil.getImagePathList(requireContext(),getActivity());
        if (imageList.size()>0) {
            imageRecyclerAdapter = new ImageRecyclerAdapter(getContext(),imageList);
        }
        imageRecyclerView.setAdapter(imageRecyclerAdapter);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        imageRecyclerView.setLayoutManager(layoutManager);

        imageRecyclerView.scrollToPosition(0);
        imageRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_fall_down));
        imageRecyclerView.scheduleLayoutAnimation();
        return view;
    }


}