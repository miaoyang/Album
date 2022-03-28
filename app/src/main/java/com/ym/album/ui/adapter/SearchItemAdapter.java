package com.ym.album.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ym.album.ui.bean.SearchBean;
import com.ym.album.R;
import java.util.ArrayList;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/4 17:23
 */
public class SearchItemAdapter extends ArrayAdapter<SearchBean> {
    private int resourceId;

    public SearchItemAdapter(Context context,int resourceId, ArrayList<SearchBean> searchBeanList){
        super(context,resourceId,searchBeanList);
        this.resourceId = resourceId;
    }

    public SearchItemAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SearchBean bean = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView searchItemImg = view.findViewById(R.id.iv_search_item_img);
        TextView searchItemRes = view.findViewById(R.id.tv_search_item_res);
        if (bean!=null){
            searchItemImg.setImageResource(bean.getIconId());
            searchItemRes.setText(bean.getContent());
        }
        return view;
    }
}
