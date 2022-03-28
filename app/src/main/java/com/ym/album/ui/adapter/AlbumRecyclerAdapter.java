package com.ym.album.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.ym.album.R;
import com.ym.album.ui.bean.AlbumBean;
import com.ym.album.utils.StringUtil;
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
        LogUtil.d(TAG,"onBindViewHolder(): albumHashMap "+albumArrayList);
        if (albumArrayList!=null && !albumArrayList.isEmpty()) {
            String albumName = albumArrayList.get(position).getAlbumName();
            LogUtil.d(TAG,"onBindViewHolder(): albumName="+albumArrayList.get(position).getAlbumName());
            LogUtil.d(TAG,"onBindViewHolder(): size="+albumArrayList.get(position).getAlbumArrayList().size());
            albumViewHolder.tvAlbumName.setText((CharSequence) albumArrayList.get(position).getAlbumName());
            albumViewHolder.tvAlbumImageNum.setText(String.valueOf(albumArrayList.get(position).getAlbumArrayList().size()));
            LogUtil.d(TAG,"position="+albumArrayList.get(position));
            // TODO
            String loadImageStr = getImage(position);
            LogUtil.d(TAG,"onBindViewHolder(): loadImageStr="+loadImageStr);
            LogUtil.d(TAG,"onBindViewHolder(): isImage="+StringUtil.isImage(loadImageStr)+
                    " StringUtil.isVideo()="+StringUtil.isVideo(loadImageStr));
            // 添加监听器
            final RequestListener mRequestListener = new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    LogUtil.d(TAG, "onBindViewHolder(): onException: " + e.toString()+"  model:"+model+" isFirstResource: "+isFirstResource);
                    albumViewHolder.ivFirstImage.setImageResource(R.mipmap.ic_launcher);
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    Log.e(TAG,  "onBindViewHolder(): model:"+model+" isFirstResource: "+isFirstResource);
                    return false;
                }
            };
            if (StringUtil.isImage(loadImageStr)) {
                if (!TextUtils.isEmpty(loadImageStr)) {
                    Glide.with(context)
                            .asBitmap()
                            .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(20)))
                            .load(loadImageStr)
                            .listener(mRequestListener)
                            .into(albumViewHolder.ivFirstImage);
                }
            }
            if (StringUtil.isVideo(loadImageStr)){
                albumViewHolder.gsyVideoPlayer.setVisibility(View.INVISIBLE);
                albumViewHolder.gsyVideoPlayer.setUp(loadImageStr, false, albumName);
                //增加title
                albumViewHolder.gsyVideoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
                //设置返回键
                albumViewHolder.gsyVideoPlayer.getBackButton().setVisibility(View.VISIBLE);
                //是否可以滑动调整
                albumViewHolder.gsyVideoPlayer.setIsTouchWiget(true);
                //设置返回按键功能
                albumViewHolder.gsyVideoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        albumViewHolder.gsyVideoPlayer.setVideoAllCallBack(null);
                    }
                });
                albumViewHolder.gsyVideoPlayer.startPlayLogic();
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

    /**
     * 当数据改变时，通知adapter改变
     * @param albumBeans
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<AlbumBean> albumBeans){
        this.albumArrayList = albumBeans;
        notifyDataSetChanged();
    }

    private static class AlbumViewHolder extends RecyclerView.ViewHolder{
        private TextView tvAlbumName;
        private TextView tvAlbumImageNum;
        private ImageView ivFirstImage;
        private StandardGSYVideoPlayer gsyVideoPlayer;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAlbumName = itemView.findViewById(R.id.tv_album_name);
            tvAlbumImageNum = itemView.findViewById(R.id.tv_album_image_num);
            ivFirstImage = itemView.findViewById(R.id.iv_item_album_image);
            gsyVideoPlayer = itemView.findViewById(R.id.gsy_video);
        }
    }
}
