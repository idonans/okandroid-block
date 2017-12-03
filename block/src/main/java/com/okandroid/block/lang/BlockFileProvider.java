package com.okandroid.block.lang;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import com.okandroid.block.AppInit;
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
        Context context = ContextUtil.getContext();
        final String authority = context.getPackageName() + ".okandroid.block.fileProvider";
        return FileProvider.getUriForFile(context, authority, file);
    }
}
