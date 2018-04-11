package com.okandroid.block.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import com.okandroid.block.R;

import timber.log.Timber;

/**
 * 辅助处理自定义 window insets, 屏蔽版本差异 {@link #onFitInsets(int, int, int, int)}
 */
public class FitInsetsLayout extends FrameLayout {

    private static final boolean DEBUG = true;

    public FitInsetsLayout(Context context) {
        this(context, null);
    }

    public FitInsetsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FitInsetsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FitInsetsLayout(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public static final int NONE = -1;
    public static final int ALL = -2;

    private int mFitInsetPaddingMaxLeft = NONE;
    private int mFitInsetPaddingMinLeft = NONE;
    private int mFitInsetPaddingMaxTop = NONE;
    private int mFitInsetPaddingMinTop = NONE;
    private int mFitInsetPaddingMaxRight = NONE;
    private int mFitInsetPaddingMinRight = NONE;
    private int mFitInsetPaddingMaxBottom = NONE;
    private int mFitInsetPaddingMinBottom = NONE;

    protected void init(
            Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray a =
                context.obtainStyledAttributes(
                        attrs, R.styleable.FitInsetsLayout, defStyleAttr, defStyleRes);

        mFitInsetPaddingMaxLeft =
                a.getLayoutDimension(
                        R.styleable.FitInsetsLayout_systemInsetPaddingMaxLeft,
                        mFitInsetPaddingMaxLeft);
        mFitInsetPaddingMinLeft =
                a.getLayoutDimension(
                        R.styleable.FitInsetsLayout_systemInsetPaddingMinLeft,
                        mFitInsetPaddingMinLeft);
        mFitInsetPaddingMaxTop =
                a.getLayoutDimension(
                        R.styleable.FitInsetsLayout_systemInsetPaddingMaxTop,
                        mFitInsetPaddingMaxTop);
        mFitInsetPaddingMinTop =
                a.getLayoutDimension(
                        R.styleable.FitInsetsLayout_systemInsetPaddingMinTop,
                        mFitInsetPaddingMinTop);
        mFitInsetPaddingMaxRight =
                a.getLayoutDimension(
                        R.styleable.FitInsetsLayout_systemInsetPaddingMaxRight,
                        mFitInsetPaddingMaxRight);
        mFitInsetPaddingMinRight =
                a.getLayoutDimension(
                        R.styleable.FitInsetsLayout_systemInsetPaddingMinRight,
                        mFitInsetPaddingMinRight);
        mFitInsetPaddingMaxBottom =
                a.getLayoutDimension(
                        R.styleable.FitInsetsLayout_systemInsetPaddingMaxBottom,
                        mFitInsetPaddingMaxBottom);
        mFitInsetPaddingMinBottom =
                a.getLayoutDimension(
                        R.styleable.FitInsetsLayout_systemInsetPaddingMinBottom,
                        mFitInsetPaddingMinBottom);

        a.recycle();

        if (DEBUG) {
            Timber.d("fit inset padding max " + getFitInsetPaddingMax());
            Timber.d("fit inset padding min " + getFitInsetPaddingMin());
        }
    }

    public Rect getFitInsetPaddingMax() {
        return new Rect(
                mFitInsetPaddingMaxLeft,
                mFitInsetPaddingMaxTop,
                mFitInsetPaddingMaxRight,
                mFitInsetPaddingMaxBottom);
    }

    public void setFitInsetPaddingMax(int left, int top, int right, int bottom) {
        if (mFitInsetPaddingMaxLeft != left
                || mFitInsetPaddingMaxTop != top
                || mFitInsetPaddingMaxRight != right
                || mFitInsetPaddingMaxBottom != bottom) {
            mFitInsetPaddingMaxLeft = left;
            mFitInsetPaddingMaxTop = top;
            mFitInsetPaddingMaxRight = right;
            mFitInsetPaddingMaxBottom = bottom;
            ViewCompat.requestApplyInsets(this);
        }
    }

    public Rect getFitInsetPaddingMin() {
        return new Rect(
                mFitInsetPaddingMinLeft,
                mFitInsetPaddingMinTop,
                mFitInsetPaddingMinRight,
                mFitInsetPaddingMinBottom);
    }

    public void setFitInsetPaddingMin(int left, int top, int right, int bottom) {
        if (mFitInsetPaddingMinLeft != left
                || mFitInsetPaddingMinTop != top
                || mFitInsetPaddingMinRight != right
                || mFitInsetPaddingMinBottom != bottom) {
            mFitInsetPaddingMinLeft = left;
            mFitInsetPaddingMinTop = top;
            mFitInsetPaddingMinRight = right;
            mFitInsetPaddingMinBottom = bottom;
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

    /**
     * @return 返回剩余的 insets 值
     */
    protected Rect onFitInsets(int left, int top, int right, int bottom) {
        Rect insetsPadding = new Rect();

        insetsPadding.left =
                calculateInsetPaddingValueConsumed(
                        left, mFitInsetPaddingMinLeft, mFitInsetPaddingMaxLeft);
        insetsPadding.top =
                calculateInsetPaddingValueConsumed(
                        top, mFitInsetPaddingMinTop, mFitInsetPaddingMaxTop);
        insetsPadding.right =
                calculateInsetPaddingValueConsumed(
                        right, mFitInsetPaddingMinRight, mFitInsetPaddingMaxRight);
        insetsPadding.bottom =
                calculateInsetPaddingValueConsumed(
                        bottom, mFitInsetPaddingMinBottom, mFitInsetPaddingMaxBottom);

        setPadding(
                insetsPadding.left, insetsPadding.top, insetsPadding.right, insetsPadding.bottom);

        Rect remain =
                new Rect(
                        left - insetsPadding.left,
                        top - insetsPadding.top,
                        right - insetsPadding.right,
                        bottom - insetsPadding.bottom);

        if (DEBUG) {
            Timber.d(
                    "onFitInsets" +
                            new Rect(left, top, right, bottom),
                    "->" +
                            "consumed:" +
                            insetsPadding +
                            "remain:" +
                            remain +
                            "min:" +
                            getFitInsetPaddingMin() +
                            "max:" +
                            getFitInsetPaddingMax());
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
