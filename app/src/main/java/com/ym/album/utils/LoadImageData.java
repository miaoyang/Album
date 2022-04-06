package com.ym.album.utils;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.ym.album.app.config.AppConstant;
import com.ym.album.event.Event;
import com.ym.album.event.EventBusUtil;
import com.ym.album.ui.bean.ImageFolderBean;
import com.ym.album.ui.bean.ImageItemBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ym.album.R;
import com.ym.common_util.utils.LogUtil;


public class LoadImageData implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "LoadImageData";

    public static final int LOADER_ALL = 0;         //加载所有图片
    public static final int LOADER_CATEGORY = 1;    //分类加载图片
    private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间，long型  1450518608

    private FragmentActivity activity;
    private OnImagesLoadedListener loadedListener;                     //图片加载完成的回调接口
    private ArrayList<ImageFolderBean> imageFolders = new ArrayList<>();   //所有的图片文件夹
    private ArrayList<ImageItemBean> imageItemBeans = new ArrayList<>();
    /**
     * @param activity       用于初始化LoaderManager
     * @param path           指定扫描的文件夹目录，可以为 null，表示扫描所有图片
     * @param loadedListener 图片加载完成的监听
     */
    public LoadImageData(FragmentActivity activity, String path, OnImagesLoadedListener loadedListener) {
        this.activity = activity;
        this.loadedListener = loadedListener;

        LoaderManager loaderManager = activity.getSupportLoaderManager();
        if (path == null) {
            loaderManager.initLoader(LOADER_ALL, null, this);//加载所有的图片
        } else {
            //加载指定目录的图片
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            loaderManager.initLoader(LOADER_CATEGORY, bundle, this);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader cursorLoader = null;
        //扫描所有图片
        if (id == LOADER_ALL)
            cursorLoader = new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC");
        //扫描某个图片文件夹
        if (id == LOADER_CATEGORY)
            cursorLoader = new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[1] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[6] + " DESC");

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        LogUtil.d(TAG,"onLoadFinished(): data="+data+" loader="+loader+" imageFolders="+imageFolders);
        imageFolders.clear();
        if (data != null) {
//            ArrayList<ImageItemBean> allImages = new ArrayList<>();   //所有图片的集合,不分文件夹
            while (data.moveToNext()) {
                //查询数据
                String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));

                File file = new File(imagePath);
                if (!file.exists() || file.length() <= 0) {
                    continue;
                }

                long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                int imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                int imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                //封装实体
                ImageItemBean imageItem = new ImageItemBean();
                imageItem.name = imageName;
                imageItem.path = imagePath;
                imageItem.size = imageSize;
                imageItem.width = imageWidth;
                imageItem.height = imageHeight;
                imageItem.mimeType = imageMimeType;
                imageItem.addTime = imageAddTime;
                imageItemBeans.add(imageItem);

                //根据父路径分类存放图片
                File imageFile = new File(imagePath);
                File imageParentFile = imageFile.getParentFile();
                ImageFolderBean imageFolder = new ImageFolderBean();
                imageFolder.name = imageParentFile.getName();
                imageFolder.path = imageParentFile.getAbsolutePath();
                // 如果当前folder不包含
                if (!imageFolders.contains(imageFolder)) {
                    ArrayList<ImageItemBean> images = new ArrayList<>();
                    images.add(imageItem);
                    imageFolder.cover = imageItem;
                    imageFolder.images = images;
                    imageFolders.add(imageFolder);
                } else {
                    imageFolders.get(imageFolders.indexOf(imageFolder)).images.add(imageItem);
                }
            }
            //防止没有图片报异常
            if (data.getCount() > 0 && imageItemBeans.size()>0) {
                //构造所有图片的集合
                ImageFolderBean allImagesFolder = new ImageFolderBean();
                allImagesFolder.name = activity.getResources().getString(R.string.ip_all_images);
                allImagesFolder.path = "/";
                allImagesFolder.cover = imageItemBeans.get(0);
                allImagesFolder.images = imageItemBeans;
                imageFolders.add(0, allImagesFolder);  //确保第一条是所有图片
            }
            LogUtil.d(TAG,"onLoadFinished(): imageItemBeans = "+imageItemBeans);
            LogUtil.d(TAG,"imageFolders(): imageFolders = "+imageItemBeans);
        }
        LogUtil.d(TAG,"onLoadFinished(): imageItemBeans = "+imageItemBeans);
        //回调接口，通知图片数据准备完成
        ImagePicker.getInstance().setImageFolders(imageFolders);
        ImagePicker.getInstance().setImageItemBeans(imageItemBeans);
        if (loadedListener!=null) {
            loadedListener.onImagesLoaded(imageFolders, imageItemBeans);
        }
        EventBusUtil.sendEvent(new Event(AppConstant.Event.LOAD_ALL_IMAGE_DATA,imageItemBeans));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        LogUtil.d(TAG,"onLoaderReset(): ");
    }

    /** 所有图片加载完成的回调接口 */
    public interface OnImagesLoadedListener {
        void onImagesLoaded(ArrayList<ImageFolderBean> imageFolders,ArrayList<ImageItemBean> imageItemBeans);
    }
}
