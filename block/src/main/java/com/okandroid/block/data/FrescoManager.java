package com.okandroid.block.data;

import android.content.Context;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.logging.FLogDefaultLoggingDelegate;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.okandroid.block.AppInit;
import com.okandroid.block.Constants;
import com.okandroid.block.lang.Singleton;
import com.okandroid.block.util.ContextUtil;
import com.okandroid.block.util.FileUtil;

import java.io.File;

import timber.log.Timber;

/**
 * fresco 图片加载. 如果有扩展卡, 则将图片换存在扩展卡上, 否则缓存在内置空间上.
 */
public class FrescoManager {

    private static final Singleton<FrescoManager> sInstance =
            new Singleton<FrescoManager>() {
                @Override
                protected FrescoManager create() {
                    return new FrescoManager();
                }
            };

    private static boolean sInit;

    public static FrescoManager getInstance() {
        FrescoManager instance = sInstance.get();
        sInit = true;
        return instance;
    }

    public static boolean isInit() {
        return sInit;
    }

    private FrescoManager() {
        Timber.v("init");
        File frescoCacheBaseDir = FileUtil.getExternalCacheDir();
        if (frescoCacheBaseDir == null) {
            frescoCacheBaseDir = FileUtil.getCacheDir();
        }

        FLogDefaultLoggingDelegate fLogDefaultLoggingDelegate =
                FLogDefaultLoggingDelegate.getInstance();
        fLogDefaultLoggingDelegate.setApplicationTag(Constants.RESOURCE_PREFIX);
        fLogDefaultLoggingDelegate.setMinimumLoggingLevel(
                AppInit.isDebug() ? Log.VERBOSE : Log.WARN);

        Context context = ContextUtil.getContext();
        ImagePipelineConfig.Builder imagePipelineConfigBuilder =
                ImagePipelineConfig.newBuilder(context)
                        .setMainDiskCacheConfig(
                                DiskCacheConfig.newBuilder(context)
                                        .setBaseDirectoryPath(frescoCacheBaseDir)
                                        .setBaseDirectoryName(
                                                "fresco_main_disk_"
                                                        + ProcessManager.getInstance()
                                                        .getProcessTag())
                                        .build())
                        .setSmallImageDiskCacheConfig(
                                DiskCacheConfig.newBuilder(context)
                                        .setBaseDirectoryPath(frescoCacheBaseDir)
                                        .setBaseDirectoryName(
                                                "fresco_small_disk_"
                                                        + ProcessManager.getInstance()
                                                        .getProcessTag())
                                        .build())
                        .setNetworkFetcher(
                                new OkHttpNetworkFetcher(
                                        OkHttpManager.getInstance().getOkHttpClient()))
                        .setDownsampleEnabled(true);

        Fresco.initialize(context, imagePipelineConfigBuilder.build());
    }
}
