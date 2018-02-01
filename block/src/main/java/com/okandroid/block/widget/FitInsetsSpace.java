package com.okandroid.block.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.Space;
import android.util.AttributeSet;
import android.view.WindowInsets;

import com.okandroid.block.R;
import com.okandroid.block.lang.ClassName;
import com.okandroid.block.lang.Log;

/** 辅助处理自定义 window insets, 屏蔽版本差异 {@link #onFitInsets(int, int, int, int)} */
public class FitInsetsSpace extends Space {

    private static final boolean DEBUG = true;
    private final String CLASS_NAME = ClassName.valueOf(this);

    public FitInsetsSpace(Context context) {
        this(context, null);
    }

    public FitInsetsSpace(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FitInsetsSpace(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, 0);
    }

    public static final int NONE = -1;
    public static final int ALL = -2;

    private int mFitInsetSpaceMaxLeft = NONE;
    private int mFitInsetSpaceMinLeft = NONE;
    private int mFitInsetSpaceMaxTop = NONE;
    private int mFitInsetSpaceMinTop = NONE;
    private int mFitInsetSpaceMaxRight = NONE;
    private int mFitInsetSpaceMinRight = NONE;
    private int mFitInsetSpaceMaxBottom = NONE;
    private int mFitInsetSpaceMinBottom = NONE;

    protected void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        final TypedArray a =
                context.obtainStyledAttributes(attrs, R.styleable.FitInsetsSpace, defStyleAttr, 0);

        mFitInsetSpaceMaxLeft =
                a.getLayoutDimension(
                        R.styleable.FitInsetsSpace_systemInsetMaxLeft, mFitInsetSpaceMaxLeft);
        mFitInsetSpaceMinLeft =
                a.getLayoutDimension(
                        R.styleable.FitInsetsSpace_systemInsetMinLeft, mFitInsetSpaceMinLeft);
        mFitInsetSpaceMaxTop =
                a.getLayoutDimension(
                        R.styleable.FitInsetsSpace_systemInsetMaxTop, mFitInsetSpaceMaxTop);
        mFitInsetSpaceMinTop =
                a.getLayoutDimension(
                        R.styleable.FitInsetsSpace_systemInsetMinTop, mFitInsetSpaceMinTop);
        mFitInsetSpaceMaxRight =
                a.getLayoutDimension(
                        R.styleable.FitInsetsSpace_systemInsetMaxRight, mFitInsetSpaceMaxRight);
        mFitInsetSpaceMinRight =
                a.getLayoutDimension(
                        R.styleable.FitInsetsSpace_systemInsetMinRight, mFitInsetSpaceMinRight);
        mFitInsetSpaceMaxBottom =
                a.getLayoutDimension(
                        R.styleable.FitInsetsSpace_systemInsetMaxBottom, mFitInsetSpaceMaxBottom);
        mFitInsetSpaceMinBottom =
                a.getLayoutDimension(
                        R.styleable.FitInsetsSpace_systemInsetMinBottom, mFitInsetSpaceMinBottom);

        a.recycle();

        if (DEBUG) {
            Log.d(CLASS_NAME, "fit inset space max", getFitInsetSpaceMax());
            Log.d(CLASS_NAME, "fit inset space min", getFitInsetSpaceMin());
        }
    }

    public Rect getFitInsetSpaceMax() {
        return new Rect(
                mFitInsetSpaceMaxLeft,
                mFitInsetSpaceMaxTop,
                mFitInsetSpaceMaxRight,
                mFitInsetSpaceMaxBottom);
    }

    public void setFitInsetSpaceMax(int left, int top, int right, int bottom) {
        if (mFitInsetSpaceMaxLeft != left
                || mFitInsetSpaceMaxTop != top
                || mFitInsetSpaceMaxRight != right
                || mFitInsetSpaceMaxBottom != bottom) {
            mFitInsetSpaceMaxLeft = left;
            mFitInsetSpaceMaxTop = top;
            mFitInsetSpaceMaxRight = right;
            mFitInsetSpaceMaxBottom = bottom;
            ViewCompat.requestApplyInsets(this);
        }
    }

    public Rect getFitInsetSpaceMin() {
        return new Rect(
                mFitInsetSpaceMinLeft,
                mFitInsetSpaceMinTop,
                mFitInsetSpaceMinRight,
                mFitInsetSpaceMinBottom);
    }

    public void setFitInsetSpaceMin(int left, int top, int right, int bottom) {
        if (mFitInsetSpaceMinLeft != left
                || mFitInsetSpaceMinTop != top
                || mFitInsetSpaceMinRight != right
                || mFitInsetSpaceMinBottom != bottom) {
            mFitInsetSpaceMinLeft = left;
            mFitInsetSpaceMinTop = top;
            mFitInsetSpaceMinRight = right;
            mFitInsetSpaceMinBottom = bottom;
            ViewCompat.requestApplyInsets(this);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        int insetLeft = insets.getSystemWindowInsetLeft();
        int insetTop = insets.getSystemWindowInsetTop();
        int insetRight = insets.getSystemWindowInsetRight();
        int insetBottom = insets.getSystemWindowInsetBottom();

        Rect remain = onFitInsets(insetLeft, insetTop, insetRight, insetBottom);

        insets.replaceSystemWindowInsets(remain.left, remain.top, remain.right, remain.bottom);

        return insets;
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        int insetLeft = insets.left;
        int insetTop = insets.top;
        int insetRight = insets.right;
        int insetBottom = insets.bottom;

        Rect remain = onFitInsets(insetLeft, insetTop, insetRight, insetBottom);

        insets.set(remain);

        return false;
    }

    /** @return 返回剩余的 insets 值 */
    protected Rect onFitInsets(int left, int top, int right, int bottom) {
        Rect insetsPadding = new Rect();

        insetsPadding.left =
                calculateInsetPaddingValueConsumed(
                        left, mFitInsetSpaceMinLeft, mFitInsetSpaceMaxLeft);
        insetsPadding.top =
                calculateInsetPaddingValueConsumed(
                        left, mFitInsetSpaceMinTop, mFitInsetSpaceMaxTop);
        insetsPadding.right =
                calculateInsetPaddingValueConsumed(
                        left, mFitInsetSpaceMinRight, mFitInsetSpaceMaxRight);
        insetsPadding.bottom =
                calculateInsetPaddingValueConsumed(
                        left, mFitInsetSpaceMinBottom, mFitInsetSpaceMaxBottom);

        if (!insetsPadding.equals(
                new Rect(
                        getPaddingLeft(),
                        getPaddingTop(),
                        getPaddingRight(),
                        getPaddingBottom()))) {
            setPadding(
                    insetsPadding.left,
                    insetsPadding.top,
                    insetsPadding.right,
                    insetsPadding.bottom);
        }

        Rect remain =
                new Rect(
                        left - insetsPadding.left,
                        top - insetsPadding.top,
                        right - insetsPadding.right,
                        bottom - insetsPadding.bottom);

        if (DEBUG) {
            Log.d(
                    CLASS_NAME,
                    "onFitInsets",
                    new Rect(left, top, right, bottom),
                    "->",
                    "consumed:",
                    insetsPadding,
                    "remain:",
                    remain,
                    "min:",
                    getFitInsetSpaceMin(),
                    "max:",
                    getFitInsetSpaceMax());
        }

        return remain;
    }

    private int calculateInsetPaddingValueConsumed(int value, int min, int max) {
        int consumed = 0;
        int consumedMin = NONE;
        int consumedMax = NONE;
        if (value > 0) {
            if (min != NONE) {
                if (min == ALL) {
                    consumedMin = value;
                } else if (min >= 0) {
                    consumedMin = min;
                } else {
                    throw new IllegalArgumentException("invalid min value " + min);
                }
            }

            if (max != NONE) {
                if (max == ALL) {
                    consumedMax = value;
                } else if (max >= 0) {
                    consumedMax = max;
                } else {
                    throw new IllegalArgumentException("invalid max value " + max);
                }
            }

            if (consumedMin >= 0 && consumedMax >= 0) {
                // 上下限同时存在
                consumed = Math.max(consumedMin, value);
                consumed = Math.min(consumedMax, consumed);
            } else if (consumedMin >= 0) {
                // 仅存在下限
                consumed = Math.max(consumedMin, value);
            } else if (consumedMax >= 0) {
                // 仅存在上限
                consumed = Math.min(consumedMax, value);
            }
        }

        return consumed;
    }
}
