package com.ym.album.ui.bean;

import java.util.List;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/7 16:52
 */
public class RecommendItemBean {
    /**
     * 每个分类里的图像
     */
    private List<String> imgList;
    /**
     * 每个分类的介绍
     */
    private String introduce;
    /**
     * 时间
     */
    private String timeStr;

    public RecommendItemBean(List<String> imgList, String introduce, String timeStr) {
        this.imgList = imgList;
        this.introduce = introduce;
        this.timeStr = timeStr;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    @Override
    public String toString() {
        return "RecommendItemBean{" +
                "imgList=" + imgList +
                ", introduce='" + introduce + '\'' +
                ", timeStr='" + timeStr + '\'' +
                '}';
    }
}
