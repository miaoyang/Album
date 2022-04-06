package com.ym.album.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.ym.album.ui.api.GlideImageLoader;
import com.ym.album.ui.bean.ImageItemBean;
import com.ym.album.utils.ImagePicker;

import java.util.ArrayList;
import java.util.Date;

import com.ym.album.R;
import com.ym.common_util.utils.LogUtil;
import com.ym.common_util.utils.TimeUtil;

import uk.co.senab.photoview.PhotoView;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/4/1 17:37
 */
public class ImagePageAdapter extends PagerAdapter {
    private static final String TAG = "ImagePageAdapter";
    private OnPageClickListener listener;
    private ArrayList<ImageItemBean> imageItemBeans = new ArrayList<>();
    private ImagePicker imagePicker;
    private Context mContext;

    public ImagePageAdapter(Context context, ArrayList<ImageItemBean> imageItemBeanArrayList){
        this.imageItemBeans = imageItemBeanArrayList;
        imagePicker = ImagePicker.getInstance();
        mContext = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mContext);
        if (listener!=null){
            listener.pageSlide(position);
        }

        if (imageItemBeans!=null ) {
            ImageItemBean itemBean = imageItemBeans.get(position);
//            LogUtil.d(TAG,"instantiateItem(): item path="+itemBean.path);
            GlideImageLoader.getInstance().displayImagePreview(photoView.getContext(), itemBean.path, photoView, 0, 0);
            container.addView(photoView);
        }
        return photoView;
    }

    @Override
    public int getCount() {
        return imageItemBeans.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnPageClickListener{
        void pageSlide(int position); // 传入当前位置
    }

    public void setOnPageClickListener(OnPageClickListener listener){
        this.listener = listener;
    }

}
