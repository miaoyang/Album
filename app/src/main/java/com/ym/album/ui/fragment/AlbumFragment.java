package com.ym.album.ui.fragment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.ym.album.ui.adapter.AlbumRecyclerAdapter;
import com.ym.album.ui.bean.AlbumBean;
import com.ym.album.ui.bean.PerImageInfoBean;
import com.ym.common_util.utils.LogUtil;
import com.ym.common_util.utils.ThreadPoolUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class AlbumFragment extends Fragment {
    private static final String TAG = "AlbumFragment";
    /**
     * 所有图片集合
     */
    private ArrayList<PerImageInfoBean> imageAllList= new ArrayList<>();
    /**
     * 相册集合
     */
    private ArrayList<AlbumBean> albumAllList = new ArrayList<>();

    private RecyclerView recyclerView;
    private AlbumRecyclerAdapter albumRecyclerAdapter;

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
        recyclerView = view.findViewById(R.id.rv_album);
        getAlbumList();
        if (albumAllList != null && albumAllList.size()>0) {
            albumRecyclerAdapter = new AlbumRecyclerAdapter(getContext(),albumAllList );
        }
        recyclerView.setAdapter(albumRecyclerAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerView.scrollToPosition(0);
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_fall_down));
        recyclerView.scheduleLayoutAnimation();

        return view;
    }

    public void getAlbumList(){
        String[] projections = new String[]{
                MediaStore.Files.FileColumns._ID, MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED,
                MediaStore.MediaColumns.MIME_TYPE, MediaStore.MediaColumns.WIDTH,
                MediaStore.MediaColumns.HEIGHT, MediaStore.MediaColumns.SIZE
        };
//        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri contentUri = MediaStore.Files.getContentUri("external");
        ContentResolver contentResolver = this.getContext().getContentResolver();
        final String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC";
        final String selection = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" +
                " AND " + MediaStore.MediaColumns.SIZE + ">0";
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(contentUri, projections, selection,
                    new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)},
                    sortOrder);
        }catch (Exception e){
            LogUtil.e(TAG,"getAlbumList(): "+e);
            e.printStackTrace();
        }
        if (cursor != null){
            while (cursor.moveToNext()){
                @SuppressLint("Range")
                String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                @SuppressLint("Range")
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                imageAllList.add(new PerImageInfoBean(imagePath,displayName));

                String dirPath = Objects.requireNonNull(new File(imagePath).getParentFile()).getAbsolutePath();
                int flag = 0;
                for(int i=0,len = albumAllList.size();i<len;i++){
                    if (albumAllList.get(i).getDirPath().equals(dirPath)){
                        flag = 1;
                        albumAllList.get(i).addImage(new PerImageInfoBean(imagePath,displayName));
                    }
                }
                // 创建相册
                if (flag == 0){
                    ArrayList<PerImageInfoBean> list = new ArrayList<>();
                    list.add(new PerImageInfoBean(imagePath,displayName));
                    String [] dirPathStr = dirPath.split("/");
                    String albumName = dirPathStr[dirPathStr.length-1];
                    albumAllList.add(new AlbumBean(albumName,dirPath,list));
                }
            }
        }
        LogUtil.d(TAG,"getAlbumList(): albumAllList, "+albumAllList);
        LogUtil.d(TAG,"getAlbumList(): allImageList, "+imageAllList);
    }

    public HashMap<String,List<String>> getImagePathList(){
        HashMap<String, List<String>> mAllAlbum = new HashMap<>();
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

        Cursor cursor = contentResolver.query(contentUri, projections, selection,
                new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)},
                sortOrder);

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
//                Log.d(TAG,"file="+file+" parentFile="+parentFile);
                // 获取目录名作为专辑名称
                String albumName = parentFile.getName();

                List<String> data = new ArrayList<>();
                data.add(path);
                mAllAlbum.put(albumName,data);

            } while (cursor.moveToNext());

            cursor.close();
        }
        LogUtil.d(TAG,"AllAlbum path "+mAllAlbum);
        return mAllAlbum;
    }
}