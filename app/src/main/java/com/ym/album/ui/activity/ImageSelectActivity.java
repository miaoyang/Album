package com.ym.album.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ym.album.R;
import com.ym.album.base.BaseActivity;
import com.ym.album.base.BaseAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImageSelectActivity extends BaseActivity
    implements Runnable, BaseAdapter.OnItemClickListener,
        BaseAdapter.OnChildLongClickListener,
        BaseAdapter.OnChildClickListener {

    private static final String INTENT_KEY_IN_MAX_SELECT = "maxSelect";

    private static final String INTENT_KEY_OUT_IMAGE_LIST = "imageList";

    public static void start(BaseActivity activity, OnPhotoSelectListener listener){
        start(activity,1,listener);
    }

    public static void start(BaseActivity activity,int maxSelect,OnPhotoSelectListener listener){
        if (maxSelect < 1){
            throw new IllegalArgumentException("You should at least select on picture!");
        }
        Intent intent = new Intent(activity,ImageSelectActivity.class);
        intent.putExtra(INTENT_KEY_IN_MAX_SELECT,maxSelect);
        activity.startActivityForResult(intent,(resultCode, data) -> {
            if (listener == null){
                return;
            }
            if (data == null){
                listener.onCancel();
                return;
            }

            ArrayList<String> list = data.getStringArrayListExtra(INTENT_KEY_OUT_IMAGE_LIST);
            if (list == null || list.isEmpty()){
                listener.onCancel();
                return;
            }

            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()){
                if (!new File(iterator.next()).isFile()){
                    iterator.remove();
                }
            }

            if (resultCode == RESULT_OK && !list.isEmpty()){
                listener.onSelected(list);
                return;
            }
            listener.onCancel();

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {

    }

    @Override
    public void onChildClick(RecyclerView recyclerView, View childView, int position) {

    }

    @Override
    public boolean onChildLongClick(RecyclerView recyclerView, View childView, int position) {
        return false;
    }

    @Override
    public void run() {

    }

    /**
     * 图片选择监听
     */
    public interface OnPhotoSelectListener {

        /**
         * 选择回调
         *
         * @param data          图片列表
         */
        void onSelected(List<String> data);

        /**
         * 取消回调
         */
        default void onCancel() {}
    }
}