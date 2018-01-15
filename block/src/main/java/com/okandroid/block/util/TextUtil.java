package com.okandroid.block.util;

import android.text.TextUtils;

import com.okandroid.block.lang.Charsets;

public class TextUtil {

    private TextUtil() {}

    public static int getGBKLength(String input) {
        if (TextUtils.isEmpty(input)) {
            return 0;
        }

        int length = input.length();
        try {
            length = input.getBytes(Charsets.GBK).length;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return length;
    }
}
