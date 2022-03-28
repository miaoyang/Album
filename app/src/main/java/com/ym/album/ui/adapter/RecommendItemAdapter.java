package com.ym.album.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ym.album.ui.bean.RecommendItemBean;
import com.ym.album.ui.widget.CustomImageView;

import java.util.List;
import com.ym.album.R;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/7 16:50
 */
public class RecommendItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecommendItemAdapter";
    private Context mContext;
    private List<RecommendItemBean> itemBeanList;

    public RecommendItemAdapter(Context context, List<RecommendItemBean> itemBeanList) {
        this.mContext = context;
        this.itemBeanList = itemBeanList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recommend_image_layout,parent,false);
        final RecommendItemViewHolder holder = new RecommendItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecommendItemViewHolder viewHolder = (RecommendItemViewHolder) holder;
        RecommendItemBean bean = itemBeanList.get(position);
        if (bean!=null) {
            // 轮播的方式，每一秒钟自动替换图片
            Glide.with(mContext)
                    .asBitmap()
                    .load(bean.getImgList().get(0))
                    .into(viewHolder.customImageView);
            viewHolder.tvTime.setText(bean.getTimeStr());
            viewHolder.tvIntroduce.setText(bean.getIntroduce());
        }
    }

    @Override
    public int getItemCount() {
        return itemBeanList!=null?itemBeanList.size():0;
    }

    private static class RecommendItemViewHolder extends RecyclerView.ViewHolder{
        private CustomImageView customImageView;
        private TextView tvIntroduce;
        private TextView tvTime;

        public RecommendItemViewHolder(@NonNull View itemView) {
            super(itemView);
            customImageView = itemView.findViewById(R.id.iv_item_recommend_image);
            tvIntroduce = itemView.findViewById(R.id.tv_item_recommend_introduce);
            tvTime = itemView.findViewById(R.id.tv_item_recommend_time);
        }
    }
}
