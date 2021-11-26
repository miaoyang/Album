package com.ym.album.ui.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.ym.album.R;

public class DecRadioButton extends RadioButton {
    private float mImageWidth;
    private float mImageHeight;

    public DecRadioButton(Context context) {
        super(context);
    }

    public DecRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DecRadioButton);
        mImageWidth = typedArray.getDimension(R.styleable.DecRadioButton_rb_width,25);
        mImageHeight = typedArray.getDimension(R.styleable.DecRadioButton_rb_height,25);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Drawable drawableLeft = this.getCompoundDrawables()[0];
        Drawable drawableTop = this.getCompoundDrawables()[1];
        Drawable drawableRight = this.getCompoundDrawables()[2];
        Drawable drawableBottom = this.getCompoundDrawables()[3];
        if (drawableLeft !=null){
            drawableLeft.setBounds(0,0,(int)mImageWidth,(int)mImageHeight);
            this.setCompoundDrawables(drawableLeft,null,null,null);
        }
        if (drawableTop !=null){
            drawableTop.setBounds(0,0,(int)mImageWidth,(int)mImageHeight);
            this.setCompoundDrawables(null,drawableTop,null,null);
        }
        if (drawableRight !=null){
            drawableRight.setBounds(0,0,(int)mImageWidth,(int)mImageHeight);
            this.setCompoundDrawables(null,null,drawableRight,null);
        }
        if (drawableBottom !=null){
            drawableBottom.setBounds(0,0,(int)mImageWidth,(int)mImageHeight);
            this.setCompoundDrawables(null,null,null,drawableBottom);
        }
    }
}
