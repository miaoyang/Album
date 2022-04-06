package com.ym.album.utils;

import com.ym.album.ui.bean.ImageFolderBean;
import com.ym.album.ui.bean.ImageItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/31 16:38
 */
public class ImagePicker {
    private static final String TAG = "ImagePicker";
    private static volatile ImagePicker sInstance;
    private ArrayList<ImageItemBean> mSelectedImages = new ArrayList<>();   //选中的图片集合
    private List<OnImageSelectedListener> mImageSelectedListeners;          // 图片选中的监听回调
    private int itemSize = 84;

    private List<ImageFolderBean> mImageFolders;   //所有的图片文件夹
    private List<ImageItemBean> mImageItemBeans;   //所有的图片
    private int mCurrentImageFolderPosition = 0;  //当前选中的文件夹位置 0表示所有图片

    private int selectImageLimit = 9;             //默认9张图片

    private ImagePicker(){}

    public static ImagePicker getInstance(){
        if (sInstance == null){
            synchronized (ImagePicker.class){
                if (sInstance==null){
                    sInstance = new ImagePicker();
                }
            }
        }
        return sInstance;
    }

    /**
     * 是否包含某item
     * @param itemBean
     * @return
     */
    public boolean isSelect(ImageItemBean itemBean){
        return mSelectedImages.contains(itemBean);
    }

    /**
     * 计算已选中的数量
     * @return
     */
    public int getSelectImageItemCount(){
        if (mSelectedImages==null)return 0;
        return mSelectedImages.size();
    }

    public ArrayList<ImageItemBean> getSelectedImage(){
        return mSelectedImages;
    }

    public void clearSelectedImage(){
        if (mSelectedImages!=null){
            mSelectedImages.clear();
        }
    }

    public void addSelectedImageItem(int position, ImageItemBean item, boolean isAdd) {
        if (isAdd) mSelectedImages.add(item);
        else mSelectedImages.remove(item);
        notifyImageSelectedChanged(position, item, isAdd);
    }

    public void setSelectedImages(ArrayList<ImageItemBean> selectedImages) {
        if (selectedImages == null) {
            return;
        }
        this.mSelectedImages = selectedImages;
    }

    private void notifyImageSelectedChanged(int position, ImageItemBean item, boolean isAdd) {
        if (mImageSelectedListeners == null) return;
        for (OnImageSelectedListener l : mImageSelectedListeners) {
            l.onImageSelected(position, item, isAdd);
        }
    }

    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
    }

    public int getSelectImageLimit() {
        return selectImageLimit;
    }

    public void setSelectImageLimit(int selectImageLimit) {
        this.selectImageLimit = selectImageLimit;
    }

    public List<ImageFolderBean> getImageFolders() {
        return mImageFolders;
    }

    public void setImageFolders(List<ImageFolderBean> imageFolders) {
        mImageFolders = imageFolders;
    }

    public List<ImageItemBean> getImageItemBeans() {
        return mImageItemBeans;
    }

    public void setImageItemBeans(List<ImageItemBean> mImageItemBeans) {
        this.mImageItemBeans = mImageItemBeans;
    }

    public ArrayList<ImageItemBean> getCurrentImageFolderItems() {
        return mImageFolders.get(mCurrentImageFolderPosition).images;
    }

    public int getCurrentImageFolderPosition() {
        return mCurrentImageFolderPosition;
    }

    public void setCurrentImageFolderPosition(int mCurrentSelectedImageSetPosition) {
        mCurrentImageFolderPosition = mCurrentSelectedImageSetPosition;
    }

    public void clear() {
        if (mImageSelectedListeners != null) {
            mImageSelectedListeners.clear();
            mImageSelectedListeners = null;
        }
        if (mImageFolders != null) {
            mImageFolders.clear();
            mImageFolders = null;
        }
        if (mSelectedImages != null) {
            mSelectedImages.clear();
        }
        mCurrentImageFolderPosition = 0;
    }

    /**
     * 图片选中的监听
     */
    public interface OnImageSelectedListener {
        void onImageSelected(int position, ImageItemBean item, boolean isAdd);
    }

    public void addOnImageSelectedListener(OnImageSelectedListener l) {
        if (mImageSelectedListeners == null) mImageSelectedListeners = new ArrayList<>();
        mImageSelectedListeners.add(l);
    }

    public void removeOnImageSelectedListener(OnImageSelectedListener l) {
        if (mImageSelectedListeners == null) return;
        mImageSelectedListeners.remove(l);
    }
}
