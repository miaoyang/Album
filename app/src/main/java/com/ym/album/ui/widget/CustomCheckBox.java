package com.ym.album.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/30 20:56
 */
public class CustomCheckBox extends AppCompatCheckBox {
    public CustomCheckBox(@NonNull Context context) {
        this(context,null);
    }

    public CustomCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomCheckBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        final boolean handled =  super.performClick();
        if (!handled){
            playSoundEffect(SoundEffectConstants.CLICK);
        }
        return handled;
    }
}
