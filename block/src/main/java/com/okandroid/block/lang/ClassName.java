package com.okandroid.block.lang;

public class ClassName {
    public static String valueOf(Object object) {
        return object.getClass().getSimpleName() + "@" + object.hashCode();
    }
}
