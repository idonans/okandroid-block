package com.okandroid.demo.block;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import com.okandroid.block.widget.FitInsetsLayout;

public class FitUntopInsetsToParent extends FitInsetsLayout {
    public FitUntopInsetsToParent(Context context) {
        super(context);
    }

    public FitUntopInsetsToParent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FitUntopInsetsToParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected Rect onFitInsets(int left, int top, int right, int bottom) {
        setParentPadding(left, 0, right, bottom);
        return new Rect(0, top, 0, 0);
    }

    private void setParentPadding(int left, int top, int right, int bottom) {
        ViewParent parent = getParent();
        if (parent instanceof View) {
            ((View) parent).setPadding(left, top, right, bottom);
        }
    }
}
