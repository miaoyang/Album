package com.ym.album.ui.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/2 11:23
 */
public class PerImageInfoBean implements Parcelable {
    /**
     * 每张图片路径
     */
    private String mImageUrl;
    /**
     * 名称
     */
    private String mImageTitle;
    /**
     * 经度
     */
    private double mLatitude;
    /**
     * 纬度
     */
    private double mLongitude;
    /**
     * 图像的城市信息
     */
    private String imgCityAddress;
    /**
     * 每张图像的时间
     */
    private String imgTime;


    public PerImageInfoBean(String imageUrl,String imageTitle,double latitude,double longitude,
                            String imgCityAddress,String imgTime){
        this.mImageUrl = imageUrl;
        this.mImageTitle = imageTitle;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.imgCityAddress = imgCityAddress;
        this.imgTime = imgTime;
    }

    public PerImageInfoBean(Parcel parcel) {
        parcel.writeString(mImageUrl);
        parcel.writeString(mImageTitle);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeString(imgCityAddress);
        parcel.writeString(imgTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mImageUrl);
        parcel.writeString(mImageTitle);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeString(imgCityAddress);
        parcel.writeString(imgTime);
    }

    public static final Creator<PerImageInfoBean> CREATOR = new Creator<PerImageInfoBean>() {
        @Override
        public PerImageInfoBean createFromParcel(Parcel parcel) {
            return new PerImageInfoBean(parcel);
        }

        @Override
        public PerImageInfoBean[] newArray(int i) {
            return new PerImageInfoBean[i];
        }
    };

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public String getImageTitle() {
        return mImageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.mImageTitle = imageTitle;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getImgCityAddress() {
        return imgCityAddress;
    }

    public void setImgCityAddress(String imgCityAddress) {
        this.imgCityAddress = imgCityAddress;
    }

    public String getImgTime() {
        return imgTime;
    }

    public void setImgTime(String imgTime) {
        this.imgTime = imgTime;
    }
}
