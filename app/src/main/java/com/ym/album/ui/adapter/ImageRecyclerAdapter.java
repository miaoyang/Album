package com.ym.album.ui.adapter;

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
import com.ym.album.app.config.PathConfig;
import com.ym.album.ui.activity.Constant;
import com.ym.common_util.utils.ToastUtil;

import java.util.List;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<String> mStringList;

    public ImageRecyclerAdapter(Context context,List<String> stringArrayList){
        mContext = context;
        mStringList = stringArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_layout,parent,false);
        final ImageViewHolder holder = new ImageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        Glide.with(mContext)
                .asBitmap()
                .load(mStringList.get(position))
                .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(20)))
                .into(imageViewHolder.ivImage);

        imageViewHolder.ivImage.setOnClickListener(v->{
            int pos = holder.getAdapterPosition();
            ARouter.getInstance().build(PathConfig.Image.IMAGE_CLICK)
                    .withString(Constant.Image.clickItemImage,mStringList.get(holder.getAdapterPosition()))
                    .navigation();
            ToastUtil.showShort(mContext,"短按触发 pos="+pos);
        });

        imageViewHolder.ivImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int pos = holder.getAdapterPosition();
                ToastUtil.showShort(mContext,"长按触发 pos="+pos);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mStringList==null?0:mStringList.size();
    }

    public void setStringList(@NonNull List<String> data){
        mStringList = data;
        notifyDataSetChanged();
    }

    public List<String> getStringList(){
        return mStringList;
    }

   private static class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivImage;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_item_image);
        }

    }
}


