package com.okandroid.block.lang;

import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.okandroid.block.AppInit;
import com.okandroid.block.Constants;
import com.okandroid.block.util.ContextUtil;

import java.io.File;

public class BlockFileProvider extends FileProvider {

    @Override
    public boolean onCreate() {
        super.onCreate();
        AppInit.init(getContext());
        return true;
    }

    public static Uri getUriForFile(File file) {
        return FileProvider.getUriForFile(ContextUtil.getContext(), getAuthority(), file);
    }

    public static String getAuthority() {
        return Constants.RESOURCE_PREFIX + "." + ContextUtil.getContext().getPackageName() + ".BlockFileProvider";
    }

}
