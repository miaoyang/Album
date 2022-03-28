package com.ym.album.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * Author:Yangmiao
 * Desc: 自定义ImageView
 * Time:2022/3/4 19:46
 */
public class CustomImageView extends AppCompatImageView {
    private Paint mPaint;
    /**
     * 宽度和高度
     */
    private int mWidth,mHeight;
    /**
     * 圆半径大小
     */
    private int mRadius;
    /**
     * 矩形凹形大小
     */
    private RectF mRecF;
    /**
     * 圆角大小
     */
    private int mRoundRadius;
    /**
     * 图形渲染
     */
    private BitmapShader mBitmapShader;

    private Matrix mMatrix;
    /**
     * 当前类型
     */
    private int mType;

    public static final int TYPE_CIRCLE = 0;//圆形
    public static final int TYPE_ROUND = 1; //矩形
    public static final int TYPE_OVAL = 2;  //椭圆形
    public static final int DEFAULT_ROUND_RADIUS = 10;

    public CustomImageView(@NonNull Context context) {
        this(context,null);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mMatrix = new Matrix();
        mRoundRadius = DEFAULT_ROUND_RADIUS;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode==MeasureSpec.EXACTLY){
            mWidth = widthSize;
        }
        if (heightMode==MeasureSpec.EXACTLY){
            mHeight = heightSize;
        }

        if (mType==TYPE_CIRCLE){
            mWidth = Math.min(getMeasuredWidth(),getMeasuredHeight());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth,mWidth);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null==getDrawable())return;
        setBitmapShader();
        if (mType == TYPE_CIRCLE){
            canvas.drawCircle(mRadius,mRadius,mRadius,mPaint);
        }else if (mType == TYPE_ROUND){
            canvas.drawRoundRect(mRecF,mRoundRadius,mRoundRadius,mPaint);
        }else if (mType == TYPE_OVAL){
            canvas.drawOval(mRecF,mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRecF = new RectF(0,0,getWidth(),getHeight());
    }

    private void setBitmapShader(){
        Drawable drawable = getDrawable();
        Bitmap bitmap = drawableToBitmap(drawable);
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (mType==TYPE_CIRCLE){
            int bitmapSize = Math.min(bitmap.getWidth(),bitmap.getHeight());
            scale = mWidth * 1.0f/bitmapSize;
        }else if (mType==TYPE_ROUND || mType==TYPE_OVAL){
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；
            // 缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
            scale = Math.max(getWidth()*1.0f/bitmap.getWidth(),getHeight()*1.0f/bitmap.getHeight());
        }
        mMatrix.setScale(scale,scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        mPaint.setShader(mBitmapShader);
    }

    /**
     * drawable转bitmap
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable){
        if (drawable instanceof BitmapDrawable){
            BitmapDrawable bitmap = (BitmapDrawable) drawable;
            return bitmap.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,w,h);
        drawable.draw(canvas);
        return bitmap;
    }

    public int getType(){
        return mType;
    }

    public CustomImageView setType(int type){
        if (mType!=type){
            this.mType = type;
            invalidate();
        }
        return this;
    }

    public int getRoundRadius(){
        return mRoundRadius;
    }

    public CustomImageView setRoundRadius(int roundRadius){
        if (mRoundRadius != roundRadius){
            mRoundRadius = roundRadius;
            invalidate();
        }
        return this;
    }

    /**
     * 设置图片的宽
     * @param width
     * @return
     */
    public CustomImageView setImageWidth(int width){
        mWidth = width;
        return this;
    }

    /**
     * 设置图片的高
     * @param height
     * @return
     */
    public CustomImageView setImageHeight(int height){
        mHeight = height;
        return this;
    }

}
