package com.okandroid.block.data;

import android.content.Context;
import android.graphics.Bitmap;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.logging.FLogDefaultLoggingDelegate;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.okandroid.block.AppEnv;
import com.okandroid.block.lang.ClassName;
import com.okandroid.block.lang.Log;
import com.okandroid.block.util.ContextUtil;
import com.okandroid.block.util.FileUtil;
import java.io.File;

/**
 * fresco 图片加载. 如果有扩展卡，则将图片换存在扩展卡上，否则缓存在内置空间上。
 */
public class FrescoManager {

  private static class InstanceHolder {

    private static final FrescoManager sInstance = new FrescoManager();
  }

  private static boolean sInit;

  public static FrescoManager getInstance() {
    FrescoManager instance = InstanceHolder.sInstance;
    sInit = true;
    return instance;
  }

  public static boolean isInit() {
    return sInit;
  }

  private final String CLASS_NAME = ClassName.valueOf(this);

  private FrescoManager() {
    Log.v(CLASS_NAME, "init");
    File frescoCacheBaseDir = FileUtil.getExternalCacheDir();
    if (frescoCacheBaseDir == null) {
      frescoCacheBaseDir = FileUtil.getCacheDir();
    }

    FLogDefaultLoggingDelegate fLogDefaultLoggingDelegate =
        FLogDefaultLoggingDelegate.getInstance();
    fLogDefaultLoggingDelegate.setApplicationTag(AppEnv.getLogTag());
    fLogDefaultLoggingDelegate.setMinimumLoggingLevel(AppEnv.getLogLevelInt());

    Bitmap.Config config = Bitmap.Config.ARGB_8888;
    if (AppEnv.isFresco565Config()) {
      config = Bitmap.Config.RGB_565;
    }

    Context context = ContextUtil.getContext();
    ImagePipelineConfig.Builder imagePipelineConfigBuilder = ImagePipelineConfig.newBuilder(context)
        .setMainDiskCacheConfig(DiskCacheConfig.newBuilder(context)
            .setBaseDirectoryPath(frescoCacheBaseDir)
            .setBaseDirectoryName(
                "fresco_main_disk_" + ProcessManager.getInstance().getProcessTag())
            .build())
        .setSmallImageDiskCacheConfig(DiskCacheConfig.newBuilder(context)
            .setBaseDirectoryPath(frescoCacheBaseDir)
            .setBaseDirectoryName(
                "fresco_small_disk_" + ProcessManager.getInstance().getProcessTag())
            .build())
        .setNetworkFetcher(new OkHttpNetworkFetcher(OkHttpManager.getInstance().getOkHttpClient()))
        .setDownsampleEnabled(true)
        .setBitmapsConfig(config);

    Fresco.initialize(context, imagePipelineConfigBuilder.build());
  }
}
