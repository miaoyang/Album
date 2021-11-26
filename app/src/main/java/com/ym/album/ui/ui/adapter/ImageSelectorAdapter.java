package com.ym.album.ui.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ym.album.R;
import com.ym.album.base.AppAdapter;

import java.util.List;

public class ImageSelectorAdapter extends AppAdapter<String> {

    private final List<String> mSelectImage;

    public ImageSelectorAdapter(@NonNull Context context,List<String> images) {
        super(context);
        mSelectImage = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder{

        private final ImageView mImageView;
        private final CheckBox mCheckBox;

        private ViewHolder(){
            super(R.layout.image_select_item);
            mImageView = findViewById(R.id.iv_image_select_image);
            mCheckBox = findViewById(R.id.iv_image_select_check);
        }
        public ViewHolder(int id, ImageView mImageView, CheckBox mCheckBox) {
            super(id);
            this.mImageView = mImageView;
            this.mCheckBox = mCheckBox;
        }

        public ViewHolder(View itemView, ImageView mImageView, CheckBox mCheckBox) {
            super(itemView);
            this.mImageView = mImageView;
            this.mCheckBox = mCheckBox;
        }

        @Override
        public void onBindView(int position) {
            String imagePath = getItem(position);
            Glide.with(getContext())
                    .asBitmap()
                    .load(imagePath)
                    .into(mImageView);
            mCheckBox.setChecked(mSelectImage.contains(imagePath));
        }
    }

    @Override
    protected RecyclerView.LayoutManager generateDefaultLayoutManager(Context context) {
        return new GridLayoutManager(context,3);
    }
}
