package com.ym.album.ui.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/2 11:19
 */
public class AlbumBean {
    /**
     * 相册名字
     */
    private String albumName;
    /**
     * 父路径
     */
    private String dirPath;
    /**
     * 存放一个相册下的所有图片
     */
    private ArrayList<PerImageInfoBean> albumArrayList;

    public AlbumBean(String albumName, String dirPath, ArrayList<PerImageInfoBean> albumArrayList) {
        this.albumName = albumName;
        this.dirPath = dirPath;
        this.albumArrayList = albumArrayList;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public void addImage(PerImageInfoBean bean){
        albumArrayList.add(bean);
    }

    public int size(){
        return albumArrayList.size();
    }

    public ArrayList<PerImageInfoBean> getAlbumArrayList(){
        return albumArrayList;
    }

}
