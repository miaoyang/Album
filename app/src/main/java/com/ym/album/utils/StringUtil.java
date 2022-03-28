package com.ym.album.utils;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.ym.common_util.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/25 17:41
 */
public class StringUtil {
    private static final String TAG = "StringUtil";
    public static final ArrayList<String> videoList= new ArrayList<String>(
            Arrays.asList("MP4","3GP","3GPP","RM","RMVB","AVI","WMV","MOV")
    );
    public static final ArrayList<String> mp3List = new ArrayList<>(
      Arrays.asList("MP3","AAC","AAC+","eAAC","WMA","RA")
    );
    public static final ArrayList<String> imgList = new ArrayList<>(
            Arrays.asList("JPEG","PNG","GIF","BMP","WEBP","JPG","HEIC")
    );
    public static boolean isContainImage(String imgUrl,String imgType){
        if (!TextUtils.isEmpty(imgUrl) && !TextUtils.isEmpty(imgType)){
            return imgUrl.contains(imgType);
        }
        return false;
    }

    public static boolean isVideo(String url){
        if (!TextUtils.isEmpty(url)) {
            String[] strSplits= url.split("\\.");
            String type = strSplits[strSplits.length-1];
//            LogUtil.d(TAG,"isVideo(): type="+type);
            if (!TextUtils.isEmpty(type)) {
                return videoList.contains(type.toUpperCase());
            }
        }
        return false;
    }

    public static boolean isImage(String url){
        if (!TextUtils.isEmpty(url)) {
            String[] strSplits= url.split("\\.");
            String type = strSplits[strSplits.length-1];
//            LogUtil.d(TAG,"isImage(): type="+type);
            if (!TextUtils.isEmpty(type)) {
                return imgList.contains(type.toUpperCase());
            }
        }
        return false;
    }
}
