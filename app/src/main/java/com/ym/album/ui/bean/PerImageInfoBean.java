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

    public PerImageInfoBean(String imageUrl,String imageTitle){
        this.mImageUrl = imageUrl;
        this.mImageTitle = imageTitle;
    }

    public PerImageInfoBean(Parcel parcel) {
        parcel.writeString(mImageUrl);
        parcel.writeString(mImageTitle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mImageUrl);
        parcel.writeString(mImageTitle);
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
}
