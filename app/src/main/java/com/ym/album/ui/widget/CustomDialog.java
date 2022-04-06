package com.ym.album.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ym.album.utils.DimenUtil;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/4/2 15:09
 */
public class CustomDialog extends Dialog {
    private Context mContext;
    private int height,width;
    private boolean canTouchable;
    private int gravity=-1;
    private int widthLayoutParams=-100,heightLayoutParams = -100;
    private View view;

    private CustomDialog(@NonNull Context context) {
        super(context);
    }

    private CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private CustomDialog(Builder builder){
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.height = builder.height;
        this.width = builder.width;
        this.heightLayoutParams = builder.heightLayoutParams;
        this.widthLayoutParams = builder.widthLayoutParams;
        this.canTouchable = builder.canTouchable;
        this.gravity = builder.gravity;
        this.view = builder.view;
    }

    private CustomDialog(Builder builder,int resStyle){
        super(builder.mContext,resStyle);
        this.mContext = builder.mContext;
        this.height = builder.height;
        this.width = builder.width;
        this.heightLayoutParams = builder.heightLayoutParams;
        this.widthLayoutParams = builder.widthLayoutParams;
        this.canTouchable = builder.canTouchable;
        this.gravity = builder.gravity;
        this.view = builder.view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCanceledOnTouchOutside(canTouchable);
        Window win = getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        if (widthLayoutParams!=-100){
            params.width = widthLayoutParams;
        }else {
            params.width = width;
        }

        if (heightLayoutParams!=-100){
            params.height = heightLayoutParams;
        }else {
            params.height = height;
        }

        if (gravity!=-1) {
            params.gravity = gravity;
        }else {
            params.gravity = Gravity.CENTER; // 默认中间
        }
        win.setAttributes(params);
    }

    public static final class Builder{
        private Context mContext;
        private int height,width;
        private boolean canTouchable;
        private int gravity=-1;
        private int widthLayoutParams=-100,heightLayoutParams = -100;
        private View view;
        private int resStyle = -1;

        public Builder(Context context){
            this.mContext = context;
        }

        public Builder setViewLayout(int resView){
            view = LayoutInflater.from(mContext).inflate(resView,null);
            return this;
        }

        public  Builder setView(View view){
            this.view = view;
            return this;
        }

        public Builder setWidthPx(int width){
            this.width = width;
            return this;
        }

        public Builder setHeightPx(int height){
            this.height = height;
            return this;
        }

        public Builder setWidthDp(int width){
            this.width = DimenUtil.INSTANCE.dp2px(mContext,width);
            return this;
        }

        public Builder setHeightDp(int height){
            this.height = DimenUtil.INSTANCE.dp2px(mContext,height);
            return this;
        }

        public Builder setWidthRes(int widthRes){
            this.width = mContext.getResources().getDimensionPixelOffset(widthRes);
            return this;
        }

        public Builder setHeightRes(int heightRes){
            this.height = mContext.getResources().getDimensionPixelOffset(heightRes);
            return this;
        }

        public Builder setWidthLayoutParams(int layoutParams){
            this.widthLayoutParams = layoutParams;
            return this;
        }

        public Builder setHeightLayoutParams(int layoutParams){
            this.heightLayoutParams = layoutParams;
            return this;
        }

        public Builder setStyle(int resStyle){
            this.resStyle = resStyle;
            return this;
        }

        public Builder setCanTouchable(boolean canTouchable){
            this.canTouchable = canTouchable;
            return this;
        }

        public Builder setViewClickListen(int viewId,View.OnClickListener listener){
            view.findViewById(viewId).setOnClickListener(listener);
            return this;
        }

        public Builder setTouchListener(int viewId,View.OnTouchListener listener){
            view.findViewById(viewId).setOnTouchListener(listener);
            return this;
        }

        public Builder setGravity(int gravity){
            this.gravity = gravity;
            return this;
        }

        public CustomDialog build(){
            if (resStyle != -1) {
                return new CustomDialog(this,resStyle);
            }else {
                return new CustomDialog(this);
            }
        }
    }

}
