package com.okandroid.demo.block;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.okandroid.block.util.SystemUtil;
import com.okandroid.block.widget.FitInsetsLayout;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class CustomKeyboardLayout extends FitInsetsLayout {

    @BindView(R.id.keyboard_content)
    View mKeyboardContent;

    @BindView(R.id.action_close)
    View mActionClose;

    private boolean mSystemKeyboardShown;
    private boolean mForceShowCustomKeyboard;

    public CustomKeyboardLayout(Context context) {
        super(context);
    }

    public CustomKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomKeyboardLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        RxView.clicks(mActionClose)
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                setForceShowCustomKeyboard(false, null);
                                closeCustomKeyboard();
                            }
                        });
    }

    public void closeCustomKeyboard() {
        mKeyboardContent.setVisibility(View.GONE);
    }

    private void openCustomKeyboard() {
        mKeyboardContent.setVisibility(View.VISIBLE);
    }

    public void setSystemKeyboardShown(boolean systemKeyboardShown) {
        this.mSystemKeyboardShown = systemKeyboardShown;
    }

    public void setForceShowCustomKeyboard(boolean forceShowCustomKeyboard, EditText editText) {
        this.mForceShowCustomKeyboard = forceShowCustomKeyboard;
        if (mForceShowCustomKeyboard) {
            if (!mSystemKeyboardShown) {
                openCustomKeyboard();
            } else {
                if (editText != null) {
                    SystemUtil.hideSoftKeyboard(editText);
                }
            }
        }
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        if (bottom > 0) {
            // show system keyboard
            setSystemKeyboardShown(true);
            closeCustomKeyboard();
        } else {
            // show our keyboard if need
            setSystemKeyboardShown(false);
            if (mForceShowCustomKeyboard) {
                openCustomKeyboard();
            }
        }
    }

}
