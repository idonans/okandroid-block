package com.okandroid.demo.block;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.widget.Space;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowInsets;

/**
 * Created by idonans on 2017/11/15.
 */

public class FitUntopInsetsToParent extends Space {
  public FitUntopInsetsToParent(Context context) {
    super(context);
  }

  public FitUntopInsetsToParent(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public FitUntopInsetsToParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.KITKAT_WATCH) @Override
  public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
    int insetLeft = insets.getSystemWindowInsetLeft();
    int insetRight = insets.getSystemWindowInsetRight();
    int insetBottom = insets.getSystemWindowInsetBottom();

    insets.replaceSystemWindowInsets(0, insets.getSystemWindowInsetTop(), 0, 0);

    setParentPadding(insetLeft, 0, insetRight, insetBottom);

    return insets;
  }

  @Override protected boolean fitSystemWindows(Rect insets) {
    int insetLeft = insets.left;
    int insetRight = insets.right;
    int insetBottom = insets.bottom;

    insets.left = 0;
    insets.right = 0;
    insets.bottom = 0;

    setParentPadding(insetLeft, 0, insetRight, insetBottom);

    return false;
  }

  private void setParentPadding(int left, int top, int right, int bottom) {
    ViewParent parent = getParent();
    if (parent instanceof View) {
      ((View) parent).setPadding(left, top, right, bottom);
    }
  }
}
