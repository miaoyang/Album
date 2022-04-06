package com.ym.album.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ym.album.R;
import com.ym.album.app.config.AppConstant;
import com.ym.album.app.config.PathConfig;
import com.ym.album.ui.activity.Constant;
import com.ym.album.ui.api.GlideImageLoader;
import com.ym.album.ui.bean.ImageItemBean;
import com.ym.album.ui.widget.CustomCheckBox;
import com.ym.album.utils.ImagePicker;
import com.ym.common_util.utils.LogUtil;
import com.ym.common_util.utils.ToastUtil;

import java.util.ArrayList;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ImageRecyclerAdapter";
    private Context mContext;
    private ArrayList<ImageItemBean> mImageItemList;

    private ArrayList<ImageItemBean> images;       //当前需要显示的所有的图片数据
    private ArrayList<ImageItemBean> mSelectedImages; //全局保存的已经选中的图片数据

    private OnImageItemClickListener listener;

    private final int selectedLimit = 9;

    private View rootView;

    private GlideImageLoader imageLoader;
    private ImagePicker imagePicker;

    public ImageRecyclerAdapter(Context context,ArrayList<ImageItemBean> imageItemList){
        mContext = context;
        mImageItemList = imageItemList;
        imageLoader = GlideImageLoader.getInstance();
        imagePicker = ImagePicker.getInstance();
        mSelectedImages = imagePicker.getSelectedImage();
    }

    /**
     * 监听item的listener
     */
    public interface OnImageItemClickListener{
        void onImageItemClick(View view, ImageItemBean itemBean,int position,ImageViewHolder imageViewHolder);
    }

    public void setOnImageItemClickListen(OnImageItemClickListener listener){
        this.listener = listener;
    }

    /**
     * 刷新数据
     * @param list 更新的list
     */
    public void refreshData(ArrayList<ImageItemBean> list){
        if (list==null || list.size()==0){
            this.images = new ArrayList<>();
        }else {
            this.images = list;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_layout,parent,false);
        this.rootView = view;
        final ImageViewHolder holder = new ImageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ImageItemBean imageItem = images.get(position);
//        LogUtil.d(TAG,"onBindViewHolder(): imageItem="+imageItem.path);
        ImageViewHolder imageViewHolder = (ImageViewHolder) holder;

        Glide.with(mContext)
                .asBitmap()
                .load(imageItem.path)
                .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(20)))
                .into(imageViewHolder.ivImage);

        imageViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectCount = imagePicker.getSelectImageItemCount();
                LogUtil.d(TAG,"onBindViewHolder(): checkView count="+selectCount+" isCheck="+imageViewHolder.checkBox.isChecked());
                // TODO 取消checkBox
                //置反
                imageViewHolder.checkBox.setChecked(!imageViewHolder.checkBox.isChecked());

                if (!imageViewHolder.checkBox.isChecked()){
                    imageViewHolder.checkBox.setVisibility(View.GONE);
                    imageViewHolder.mask.setVisibility(View.GONE);
                    imagePicker.addSelectedImageItem(position, imageItem, false);
                }
            }
        });

        imageViewHolder.ivImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LogUtil.d(TAG,"onBindViewHolder(): onLongClick, pos="+position);
                imageViewHolder.checkBox.setVisibility(View.VISIBLE);
                imageViewHolder.checkBox.setChecked(true);

                if (imageViewHolder.checkBox.isChecked() && mSelectedImages.size() >= selectedLimit) {
                    ToastUtil.showShort(view.getContext(), mContext.getString(R.string.select_image_limit, selectedLimit));
                    imageViewHolder.checkBox.setChecked(false);
                    imageViewHolder.mask.setVisibility(View.GONE);
                    imageViewHolder.checkBox.setVisibility(View.GONE);
                } else if (!mSelectedImages.contains(imageItem)){
                    imagePicker.addSelectedImageItem(position, imageItem, imageViewHolder.checkBox.isChecked());
                    // 当选中图片时，蒙版展示
                    imageViewHolder.mask.setVisibility(View.VISIBLE);
                    LogUtil.d(TAG,"add selected image in arrayList");
                }

                if (listener != null) listener.onImageItemClick(rootView, imageItem, position,imageViewHolder);
                return true;
            }
        });

        imageViewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                ARouter.getInstance().build(PathConfig.Image.IMAGE_CLICK)
                        .withInt(AppConstant.UI.CLICK_IMAGE_ITEM,holder.getAdapterPosition())
                        .withSerializable(AppConstant.UI.DATA_IMAGE_ITEM_LIST,mImageItemList)
                        .navigation();
                ToastUtil.showShort(mContext,"短按触发 pos="+pos);
                //清空选择的数据
                if (mSelectedImages!=null){
                    mSelectedImages.clear();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mImageItemList==null?0:mImageItemList.size();
    }

   public static class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivImage;
        public View mask;
        public CustomCheckBox checkBox;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_item_image);
            mask = itemView.findViewById(R.id.item_image_mask);
            checkBox = itemView.findViewById(R.id.cb_check);
        }
    }
}


