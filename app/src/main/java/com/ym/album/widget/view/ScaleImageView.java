package com.ym.album.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.ym.album.R;

public class ScaleImageView extends AppCompatImageView {
    private float mScaleSize = 1.2f;

    public ScaleImageView(@NonNull Context context) {
        this(context,null);
    }

    public ScaleImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScaleImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView);
        setScaleSize(typedArray.getFloat(R.styleable.ScaleImageView_scaleRatio,mScaleSize));
        typedArray.recycle();
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        if (pressed){
            setScaleX(mScaleSize);
            setScaleY(mScaleSize);
        }else {
            setScaleX(1);
            setScaleY(1);
        }
    }

    public void setScaleSize(float size){
        mScaleSize = size;
    }
}
