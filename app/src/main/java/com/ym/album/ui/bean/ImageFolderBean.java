package com.ym.album.ui.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/31 17:37
 */
public class ImageFolderBean implements Serializable {
    public String name;  //当前文件夹的名字
    public String path;  //当前文件夹的路径
    public ImageItemBean cover;   //当前文件夹需要要显示的缩略图，默认为最近的一次图片
    public ArrayList<ImageItemBean> images;  //当前文件夹下所有图片的集合

    /** 只要文件夹的路径和名字相同，就认为是相同的文件夹 */
    @Override
    public boolean equals(Object o) {
        try {
            ImageFolderBean other = (ImageFolderBean) o;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
