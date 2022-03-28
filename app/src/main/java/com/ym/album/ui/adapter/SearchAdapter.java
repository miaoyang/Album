package com.ym.album.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.ym.album.ui.bean.SearchBean;

import java.util.List;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/3 19:41
 */
public class SearchAdapter extends BaseAdapter {
    private Context mContext;
    private List<SearchBean> beanList;
    private int layoutId;

    public SearchAdapter(Context mContext, List<SearchBean> beanList, int layoutId) {
        this.mContext = mContext;
        this.beanList = beanList;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return beanList.size();
    }

    @Override
    public Object getItem(int i) {
        return beanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return layoutId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(mContext).inflate(layoutId,viewGroup,false);
        return v;
    }

}
