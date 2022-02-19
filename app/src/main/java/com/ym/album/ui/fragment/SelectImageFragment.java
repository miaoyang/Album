package com.ym.album.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.ym.album.R;
import com.ym.album.base.BaseFragment;
import com.ym.album.ui.adapter.ImageRecyclerAdapter;
import com.ym.common.utils.LogUtil;

import java.io.File;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;


public class SelectImageFragment extends Fragment {
    private static final String TAG = SelectImageFragment.class.getSimpleName();
    private RecyclerView imageRecyclerView;
    private ImageRecyclerAdapter imageRecyclerAdapter;

    public SelectImageFragment() {
        // Required empty public constructor
    }

    public static SelectImageFragment newInstance() {
        SelectImageFragment fragment = new SelectImageFragment();
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
        View view =  inflater.inflate(R.layout.fragment_select_image, container, true);
        imageRecyclerView = view.findViewById(R.id.rv_img);

        List<String> imageList = getImagePathList();
        if (imageList!=null && imageList.size()>0) {
            imageRecyclerAdapter = new ImageRecyclerAdapter(getContext(),imageList);
        }
        imageRecyclerView.setAdapter(imageRecyclerAdapter);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        imageRecyclerView.setLayoutManager(layoutManager);

        imageRecyclerView.scrollToPosition(0);
        imageRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_fall_down));
        imageRecyclerView.scheduleLayoutAnimation();
        return view;
    }

    public List<String> getImagePathList(){
        List<String> imagePathList = new ArrayList<>();
        String path="";
        final Uri contentUri = MediaStore.Files.getContentUri("external");
        final String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC";
        final String selection = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" + " AND " + MediaStore.MediaColumns.SIZE + ">0";

        ContentResolver contentResolver = getActivity().getContentResolver();
        String[] projections = new String[]{
                MediaStore.Files.FileColumns._ID, MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED,
                MediaStore.MediaColumns.MIME_TYPE, MediaStore.MediaColumns.WIDTH,
                MediaStore.MediaColumns.HEIGHT, MediaStore.MediaColumns.SIZE
        };

        Cursor cursor = null;

        if (checkPermission(getContext())){
            cursor = contentResolver.query(contentUri, projections, selection,
                    new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)},
                    sortOrder);
        }
        if (cursor != null && cursor.moveToFirst()) {

            int pathIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            int mimeTypeIndex = cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE);
            int sizeIndex = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE);

            do {
                long size = cursor.getLong(sizeIndex);
                // 图片大小不得小于 1 KB
                if (size < 1024) {
                    continue;
                }

                String type = cursor.getString(mimeTypeIndex);
                path = cursor.getString(pathIndex);
                if (TextUtils.isEmpty(path) || TextUtils.isEmpty(type)) {
                    continue;
                }

                File file = new File(path);
                if (!file.exists() || !file.isFile()) {
                    continue;
                }

                File parentFile = file.getParentFile();
                if (parentFile == null) {
                    continue;
                }

                // 获取目录名作为专辑名称
                String albumName = parentFile.getName();
                imagePathList.add(path);
            } while (cursor.moveToNext());

            cursor.close();
        }
        LogUtil.d(TAG,"Image path "+imagePathList);
        return imagePathList;
    }

    private boolean checkPermission(Context context){
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        boolean isCheckPermissionTrue = true;
        for(String permission:permissions){
            if (ActivityCompat.checkSelfPermission(context,permission)==PackageManager.PERMISSION_DENIED){
                isCheckPermissionTrue = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,permission)){
                    new AlertDialog.Builder(context)
                            .setTitle("申请权限")
                            .setMessage("您将申请以下权限"+permission)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(getActivity(),permissions,0x01);
                                }
                            }).show();
                }else {
                    ActivityCompat.requestPermissions(getActivity(),permissions,0x01);
                }
                break;
            }
        }
        return isCheckPermissionTrue;
    }
}