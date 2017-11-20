package com.okandroid.block.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.widget.Space;
import android.util.AttributeSet;
import android.view.WindowInsets;

/**
 * 辅助处理自定义 window insets, 屏蔽版本差异
 * {@link #onFitInsets(int, int, int, int)}
 */
public class FitInsetsSpace extends Space {
  public FitInsetsSpace(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public FitInsetsSpace(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public FitInsetsSpace(Context context) {
    super(context);
    init();
  }

  protected void init() {
  }

  @TargetApi(Build.VERSION_CODES.KITKAT_WATCH) @Override
  public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
    int insetLeft = insets.getSystemWindowInsetLeft();
    int insetTop = insets.getSystemWindowInsetTop();
    int insetRight = insets.getSystemWindowInsetRight();
    int insetBottom = insets.getSystemWindowInsetBottom();

    Rect remain = onFitInsets(insetLeft, insetTop, insetRight, insetBottom);

    insets.replaceSystemWindowInsets(remain.left, remain.top, remain.right, remain.bottom);

    return insets;
  }

  @Override protected boolean fitSystemWindows(Rect insets) {
    int insetLeft = insets.left;
    int insetTop = insets.top;
    int insetRight = insets.right;
    int insetBottom = insets.bottom;

    Rect remain = onFitInsets(insetLeft, insetTop, insetRight, insetBottom);

    insets.set(remain);

    return false;
  }

  /**
   * @return 返回剩余的 insets 值
   */
  protected Rect onFitInsets(int left, int top, int right, int bottom) {
    return new Rect(left, top, right, bottom);
  }
}
