package com.ym.album.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ym.album.ui.bean.AppInfoItemBean;

import java.util.List;
import com.ym.album.R;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/4/6 14:16
 */
public class SharePopupWindowAdapter extends BaseAdapter {
    private Context mContext;
    private List<AppInfoItemBean> itemBeanList;
    private AdapterView.OnItemClickListener listener;

    public SharePopupWindowAdapter(Context context,List<AppInfoItemBean> itemBeanList){
        super();
        this.mContext = context;
        this.itemBeanList = itemBeanList;
    }

    @Override
    public int getCount() {
        return itemBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AppInfoItemBean bean = itemBeanList.get(i);
        View v;
        ShareViewHolder holder;
        if (view == null){
            v = LayoutInflater.from(mContext).inflate(R.layout.share_dialog_appinfo_layout,null);
            holder = new ShareViewHolder(v);
            v.setTag(holder);
        }else {
            v = view;
            holder = (ShareViewHolder) view.getTag();
        }
        holder.tvAppName.setText(bean.getAppName());
        holder.ivAppIcon.setImageDrawable(bean.getAppIcon());
        return v;
    }

    class ShareViewHolder{
        ImageView ivAppIcon;
        TextView tvAppName;

        public ShareViewHolder(View view){
            ivAppIcon = view.findViewById(R.id.iv_share_app_icon);
            tvAppName = view.findViewById(R.id.tv_share_app_name);
        }
    }
}
