package com.ym.album.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.ym.album.R;
import com.ym.album.ui.bean.AlbumBean;
import com.ym.common_util.utils.LogUtil;
import com.ym.common_util.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlbumRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = AlbumRecyclerAdapter.class.getSimpleName();

    private ArrayList<AlbumBean> albumArrayList;
    private Context context;

    public AlbumRecyclerAdapter(Context context,ArrayList<AlbumBean> albumArrayList){
        this.context = context;
        this.albumArrayList = albumArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album_layout,parent,false);
        final AlbumViewHolder holder = new AlbumViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showShort(context,"点击触发");
            }
        });
        holder.ivFirstImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                ToastUtil.showShort(context,"短按触发 pos="+pos);
            }
        });
        holder.ivFirstImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int pos = holder.getAdapterPosition();
                ToastUtil.showShort(context,"长按触发 pos="+pos);
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AlbumViewHolder albumViewHolder = (AlbumViewHolder) holder;
        LogUtil.d(TAG,"onBindViewHolder albumHashMap "+albumArrayList);
        if (albumArrayList!=null && !albumArrayList.isEmpty()) {
            LogUtil.d(TAG,"onBindViewHolder(): albumName="+albumArrayList.get(position).getAlbumName());
            LogUtil.d(TAG,"onBindViewHolder(): size="+albumArrayList.get(position).getAlbumArrayList().size());
            albumViewHolder.tvAlbumName.setText((CharSequence) albumArrayList.get(position).getAlbumName());
            albumViewHolder.tvAlbumImageNum.setText(String.valueOf(albumArrayList.get(position).getAlbumArrayList().size()));
            LogUtil.d(TAG,"position="+albumArrayList.get(position));
            // TODO
            String loadImageStr = getImage(position);
            if (!TextUtils.isEmpty(loadImageStr)) {
                Glide.with(context)
                        .asBitmap()
                        .apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(20)))
                        .load(loadImageStr)
                        .into(albumViewHolder.ivFirstImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return albumArrayList==null?0:albumArrayList.size();
    }

    private String getImage(int pos){
        if (albumArrayList != null && albumArrayList.size()>0){
            AlbumBean bean= albumArrayList.get(pos);
            if (bean != null){
                return bean.getAlbumArrayList().get(0).getImageUrl();
            }
        }
        return "";
    }

    private static class AlbumViewHolder extends RecyclerView.ViewHolder{
        private TextView tvAlbumName;
        private TextView tvAlbumImageNum;
        private ImageView ivFirstImage;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAlbumName = itemView.findViewById(R.id.tv_album_name);
            tvAlbumImageNum = itemView.findViewById(R.id.tv_album_image_num);
            ivFirstImage = itemView.findViewById(R.id.iv_item_album_image);
        }
    }
}
