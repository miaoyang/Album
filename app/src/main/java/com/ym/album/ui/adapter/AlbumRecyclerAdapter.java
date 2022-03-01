package com.ym.album.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ym.album.R;
import com.ym.common_util.utils.LogUtil;
import com.ym.common_util.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;

public class AlbumRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = AlbumRecyclerAdapter.class.getSimpleName();

    private HashMap<String, List<String>> albumHashMap;
    private Context context;

    public AlbumRecyclerAdapter(Context context,HashMap<String,List<String>> hashMap){
        this.context = context;
        this.albumHashMap = hashMap;
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
        LogUtil.d(TAG,"onBindViewHolder albumHashMap "+albumHashMap);
        if (albumHashMap!=null && !albumHashMap.isEmpty()) {
            albumViewHolder.tvAlbumName.setText((CharSequence) albumHashMap.get(position));
            albumViewHolder.tvAlbumImageNum.setText("1");
            LogUtil.d(TAG,"position="+albumHashMap.get(position));
            // TODO null object
            String loadImageStr = getImage(position);
            if (!TextUtils.isEmpty(loadImageStr)) {
                Glide.with(context)
                        .asBitmap()
                        .load(loadImageStr)
                        .into(albumViewHolder.ivFirstImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return albumHashMap==null?0:albumHashMap.size();
    }

    private String getImage(int pos){
        if (albumHashMap != null && albumHashMap.size()>0){
            List<String> list= albumHashMap.get(pos);
            if (list != null){
                return list.get(0);
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
