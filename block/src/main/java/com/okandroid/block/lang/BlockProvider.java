package com.okandroid.block.lang;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import com.okandroid.block.AppInit;
import com.okandroid.block.util.ContextUtil;
import java.io.File;

public class BlockProvider extends FileProvider {
  @Override public void attachInfo(Context context, ProviderInfo info) {
    AppInit.init(context);
    super.attachInfo(context, info);
  }

  public static Uri getUriForFile(File file) {
    Context context = ContextUtil.getContext();
    final String authority = context.getPackageName() + ".okandroid.block.provider";
    return FileProvider.getUriForFile(context, authority, file);
  }
}
