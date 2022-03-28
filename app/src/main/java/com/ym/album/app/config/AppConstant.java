package com.ym.album.app.config;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/24 20:52
 */
public class AppConstant {
    /**
     * Album
     */
    public static final String ALBUM_LIST_KEY = "album_list_all";
    public static final String IMAGE_LIST_KEY = "image_list_all";
    public static final int LOADING_IMAGE_ONE_DAY = 60*60*1000; // 1hour
    public static final String LAST_LOADING_IMAGE = "last_loading_image";

    public static final int LOGIN_SUCCESS_NOT_LOADING_IMAGE = 0;
    public static final int LOGIN_SUCCESS_LOADING_IMAGE = 1;
    public static final int ALBUM_EVENT_1 = 1;
    public static final int IMAGE_EVENT_2 = 2;

}
