package com.ym.album.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.ym.album.AlbumApp;
import com.ym.album.app.config.AppConstant;
import com.ym.album.base.IResultCallback;
import com.ym.album.event.Event;
import com.ym.album.event.EventBusUtil;
import com.ym.album.ui.bean.AlbumBean;
import com.ym.album.ui.bean.PerImageInfoBean;
import com.ym.album.ui.bean.RecommendItemBean;
import com.ym.common_util.utils.JsonUtil;
import com.ym.common_util.utils.LogUtil;
import com.ym.common_util.utils.SpUtil;
import com.ym.common_util.utils.ThreadPoolUtil;
import com.ym.common_util.utils.TimeUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/2 11:48
 */
public class ImageMediaUtil {
    public static final String TAG = "ImageMediaUtil";

    /**
     * 所有图片集合
     */
    private volatile static ArrayList<PerImageInfoBean> imageAllList = new ArrayList<>();
    /**
     * 相册集合
     */
    private volatile static ArrayList<AlbumBean> albumAllList = new ArrayList<>();


    public static List<String> getImagePathList(@NonNull Context mContext, Activity activity) {
        List<String> imagePathList = new ArrayList<>();
        String path = "";
        final Uri contentUri = MediaStore.Files.getContentUri("external");
        final String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC";
        final String selection = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" + " AND " + MediaStore.MediaColumns.SIZE + ">0";

        ContentResolver contentResolver = mContext.getContentResolver();
        String[] projections = new String[]{
                MediaStore.Files.FileColumns._ID, MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED,
                MediaStore.MediaColumns.MIME_TYPE, MediaStore.MediaColumns.WIDTH,
                MediaStore.MediaColumns.HEIGHT, MediaStore.MediaColumns.SIZE
        };

        Cursor cursor = null;

        if (checkPermission(mContext, activity)) {
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
        LogUtil.d(TAG, "getImagePathList(): Image path " + imagePathList);
        return imagePathList;
    }

    public static boolean checkPermission(Context context, Activity activity) {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_MEDIA_LOCATION
        };
        boolean isCheckPermissionTrue = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                isCheckPermissionTrue = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    new AlertDialog.Builder(context)
                            .setTitle("申请权限")
                            .setMessage("您将申请以下权限" + permission)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(activity, permissions, 0x01);
                                }
                            }).show();
                } else {
                    ActivityCompat.requestPermissions(activity, permissions, 0x01);
                }
                break;
            }
        }
        return isCheckPermissionTrue;
    }

    /**
     * 获取设备到所有图片和相册
     * @param context
     */
    public static void getAlbumList(Context context,Activity activity) {
//        String[] projections = new String[]{
//                MediaStore.Files.FileColumns._ID, MediaStore.MediaColumns.DATA,
//                MediaStore.MediaColumns.DISPLAY_NAME,MediaStore.Images.Media.TITLE,
//                MediaStore.MediaColumns.MIME_TYPE, MediaStore.MediaColumns.SIZE,
//                MediaStore.MediaColumns.HEIGHT, MediaStore.MediaColumns.WIDTH,
//                MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE,
//                MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_MODIFIED,
//        };

//        String[] projections = new String[]{
//                MediaStore.Files.FileColumns._ID, MediaStore.MediaColumns.DATA,
//                MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED,
//                MediaStore.MediaColumns.MIME_TYPE, MediaStore.MediaColumns.WIDTH,
//                MediaStore.MediaColumns.HEIGHT, MediaStore.MediaColumns.SIZE,
//        };
        final String[] projections = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATE_MODIFIED,
//                MediaStore.Images.Media.LATITUDE,
//                MediaStore.Images.Media.LONGITUDE,
                MediaStore.Images.Media.SIZE
        };

        final Uri contentUri = MediaStore.Files.getContentUri("external");
        ContentResolver contentResolver = context.getContentResolver();
        final String selection = "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" +
                " AND " + MediaStore.MediaColumns.SIZE + ">0";
        final String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC";

        Cursor cursor = null;
        ExifInterface exif =null;

        try {
            if (checkPermission(context, activity)) {
                try {
//                    cursor = contentResolver.query(contentUri, projections, selection,
//                            new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)},
//                            sortOrder);
                    cursor = contentResolver.query(contentUri, projections, null,
                            null, sortOrder);
                } catch (Exception e) {
                    LogUtil.e(TAG, "getAlbumList(): cursor ", e);
                }
            }
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // 图像路径
                    @SuppressLint("Range")
                    String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    @SuppressLint("Range")
                    String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));

                    exif = new ExifInterface(imagePath);
                    String lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                    String lon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                    LogUtil.d(TAG,"getAlbumList(): lat="+lat+" lon="+lon);

                    double[] latAndLng = strToLatAndLng(lat,lon);
                    String imgAddress = getAddress(context,latAndLng[0],latAndLng[1]);

                    // 图像时间
                    String watermarkTime = exif.getAttribute(ExifInterface.TAG_DATETIME);
                    LogUtil.d(TAG,"getAlbumList(): watermarkTime="+watermarkTime);
                    String imgTime = "";
                    if (!TextUtils.isEmpty(watermarkTime)){
                        imgTime = TimeUtil.strToStr(watermarkTime,"yyyy:MM:dd");
                    }

                    LogUtil.d(TAG,"getAlbumList(): imagePath="+imagePath+" displayName="+displayName+" latAndLng[0]="+latAndLng[0]+
                            " latAndLng[1]="+latAndLng[1]+" imgAddress="+imgAddress+" imgTime="+imgTime);
                    imageAllList.add(new PerImageInfoBean(imagePath, displayName, latAndLng[0], latAndLng[1],imgAddress,imgTime));

                    String dirPath = Objects.requireNonNull(new File(imagePath).getParentFile()).getAbsolutePath();
                    int flag = 0;
                    if (!imagePath.contains("mp3")) {
                        for (int i = 0, len = albumAllList.size(); i < len; i++) {
                            if (albumAllList.get(i).getDirPath().equals(dirPath)) {//
                                flag = 1;
                                albumAllList.get(i).addImage(new PerImageInfoBean(imagePath, displayName, latAndLng[0], latAndLng[1], imgAddress, imgTime));
                            }
                        }
                        // 创建相册
                        if (flag == 0) {
                            ArrayList<PerImageInfoBean> list = new ArrayList<>();
                            list.add(new PerImageInfoBean(imagePath, displayName, latAndLng[0], latAndLng[1],imgAddress,imgTime));
                            String[] dirPathStr = dirPath.split("/");
                            String albumName = dirPathStr[dirPathStr.length - 1];
                            albumAllList.add(new AlbumBean(albumName, dirPath, list));
                        }
                    }

                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "getAlbumList(): error, " + e);
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        LogUtil.d(TAG, "getAlbumList(): albumAllList, " + albumAllList);
        LogUtil.d(TAG, "getAlbumList(): allImageList, " + imageAllList);
        for (int i=0;i<albumAllList.size();i++){
            LogUtil.d(TAG,"i="+i+" len="+albumAllList.size());
            if (albumAllList.get(i).getAlbumArrayList().size()<2){
                albumAllList.remove(i);
            }
        }
        LogUtil.d(TAG, "getAlbumList(): allImageList<2, " + imageAllList);
        EventBusUtil.sendEvent(new Event(AppConstant.ALBUM_EVENT_1,albumAllList));
        String albumJson = JsonUtil.serialize(albumAllList);
        String imageJson = JsonUtil.serialize(imageAllList);
        SpUtil.getInstance(AlbumApp.getApp()).putString(AppConstant.ALBUM_LIST_KEY,albumJson);
        SpUtil.getInstance(AlbumApp.getApp()).putString(AppConstant.IMAGE_LIST_KEY,imageJson);

        SpUtil.getInstance(AlbumApp.getApp()).putLong(AppConstant.LAST_LOADING_IMAGE,System.currentTimeMillis());

    }

    public static ArrayList<PerImageInfoBean> getAllImageList() {
        return imageAllList;
    }

    public static ArrayList<AlbumBean> getAlbumAllList() {
        return albumAllList;
    }

    /**
     * 根据经纬度信息获取到具体地址
     * @param context
     * @param lnt
     * @param lat
     * @return
     */
    public static String getAddress(Context context, double lnt, double lat) {
        Geocoder geocoder = new Geocoder(context);
        StringBuilder sb = new StringBuilder();
        String imgAddress = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lnt, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    //每一组地址里面还会有许多地址。这里我取的前2个地址。xxx街道-xxx位置
                    if (i == 0) {
                        sb.append(address.getAddressLine(i)).append("-");
                    }
                    if (i == 1) {
                        sb.append(address.getAddressLine(i));
                        break;
                    }
                }
                imgAddress = address.getLocality();
                sb.append(address.getCountryName()).append("");//国家
                sb.append(address.getAdminArea()).append("");//省份
                sb.append(address.getLocality()).append("");//市
                sb.append(address.getFeatureName()).append("");//周边地址
                LogUtil.d(TAG, "getAddress(): image address " + sb.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgAddress;
    }

    public static List<RecommendItemBean> getRecommendItem(){
        List<RecommendItemBean> itemBeanList = new ArrayList<>();
        RecommendItemBean itemBean = null;
        for (int i=0,len = imageAllList.size();i<len;i++){
            PerImageInfoBean perImageInfoBean = imageAllList.get(i);
            String imgCity = perImageInfoBean.getImgCityAddress();

        }
        return itemBeanList;
    }

    /**
     * 将string类型的经纬度转换为double类型，并约束范围到[-90,90]
     * @param latInput
     * @param lonInput
     * @return
     */
    public static double[] strToLatAndLng(String latInput,String lonInput){
        double[] latAndLng = new double[2];
        if (!TextUtils.isEmpty(latInput) && !TextUtils.isEmpty(lonInput)) {
            latInput = latInput.replace("/", ",");
            lonInput = lonInput.replace("/", ",");

            String[] lat = latInput.split(",");
            String[] lng = lonInput.split(",");

            double latD = 0f;
            double latM = 0f;
            double latS = 0f;
            double lngD = 0f;
            double lngM = 0f;
            double lngS = 0f;

            if (lat.length >=2) {
                latD = Float.parseFloat(lat[0]) / Float.parseFloat(lat[1]);
            }
            if (lat.length >=4) {
                latM = Float.parseFloat(lat[2]) / Float.parseFloat(lat[3]);
            }
            if (lat.length >=6) {
                latS = Float.parseFloat(lat[4]) / Float.parseFloat(lat[5]);
            }
            if (lng.length >=1) {
                lngD = Float.parseFloat(lng[0]) / Float.parseFloat(lng[1]);
            }
            if (lng.length >=2) {
                lngM = Float.parseFloat(lng[2]) / Float.parseFloat(lng[3]);
            }

            if (lng.length >=3) {
                lngS = Float.parseFloat(lng[4]) / Float.parseFloat(lng[5]);
            }

            latAndLng[0] = latD + latM /60 + latS /3600;
            latAndLng[1] = lngD + lngM /60 + lngS /3600;
        }
        LogUtil.d(TAG,"strToLatAndLng(): lat="+latAndLng[0]+" lng="+latAndLng[1]);
        if (latAndLng[0]<-90){
            latAndLng[0]=-90;
        }
        if (latAndLng[0]>90){
            latAndLng[0]=90;
        }
        if (latAndLng[1]<-90){
            latAndLng[1]=-90;
        }
        if (latAndLng[1]>90){
            latAndLng[1]=90;
        }

        return latAndLng;
    }
}