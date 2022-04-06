package com.ym.album.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.app.ActionBar.LayoutParams;

import androidx.core.content.FileProvider;

import com.ym.album.R;
import com.ym.album.ui.adapter.SharePopupWindowAdapter;
import com.ym.album.ui.bean.AppInfoItemBean;
import com.ym.album.utils.DimenUtil;
import com.ym.album.utils.ShareUtil;
import com.ym.common_util.utils.LogUtil;

import java.io.File;
import java.util.List;

/**
 * Author:Yangmiao
 * Desc: 图片分享
 * Time:2022/4/6 10:51
 */
public class SharePopupWindow extends PopupWindow {
    private static final String TAG = "SharePopupWindow";

    public static final String AUTHORITY = "com.ym.album.fileprovider";
    private static final int NUM = 4;

    private View shareView;
    private GridView gridView;
    private TextView tvPopupWindowClose;

    private SharePopupWindowAdapter sharePopupWindowAdapter;

    private List<AppInfoItemBean> mAppInfoList;
    private String imgPath;
    private String shareTitle;
    private String shareContent;

    public void setImagePath(String imagePath) {
        this.imgPath = imagePath;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public SharePopupWindow(final Context context){
        super(context);
        shareView = LayoutInflater.from(context).inflate(R.layout.share_dialog_layout,null);
        gridView = shareView.findViewById(R.id.popup_window_gridview);
        tvPopupWindowClose = shareView.findViewById(R.id.tv_popup_window_close);
        mAppInfoList = ShareUtil.getAllApps(context);
        sharePopupWindowAdapter = new SharePopupWindowAdapter(context,mAppInfoList);

        gridView.setAdapter(sharePopupWindowAdapter);

        changeGridView(context);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setAction(Intent.ACTION_SEND); //Intent.ACTION_VIEW

                String imgPath = mAppInfoList.get(i).getAppIcon().toString();

                Uri uri = FileProvider.getUriForFile(context, AUTHORITY, new File(imgPath));
                intent.setDataAndType(uri, "image/*");
                context.startActivity(intent);
                LogUtil.d(TAG,"SharePopupWindow(): uri="+uri.getPath());
            }
        });

        tvPopupWindowClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //设置SelectPicPopupWindow的View
        this.setContentView(shareView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(DimenUtil.INSTANCE.getScreenHeightAndWidth(context).y/2);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置窗口外也能点击（点击外面时，窗口可以关闭）
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.circleDialog);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }

    private void changeGridView(Context context) {
        // item宽度
        int itemWidth = DimenUtil.INSTANCE.dp2px(context, 100);
        // item之间的间隔
        int itemPaddingH = DimenUtil.INSTANCE.dp2px(context, 1);
        //计算一共显示多少行;
        int size = mAppInfoList.size();
        //int row=(size<=NUM) ? 1 :( (size%NUM>0) ? size/NUM+1 : size/NUM );
        //每行真正显示多少个
        int rowItem = Math.min(size, NUM);
        // 计算GridView宽度
        int gridviewWidth = rowItem * (itemWidth + itemPaddingH);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gridView.setLayoutParams(params);
        gridView.setColumnWidth(itemWidth);
        gridView.setHorizontalSpacing(itemPaddingH);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(rowItem);
    }

}
