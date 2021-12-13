package com.ym.album.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ym.album.R;

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
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        Glide.with(mContext)
                .asBitmap()
                .load(mStringList.get(position))
                .into(imageViewHolder.ivImage);

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


