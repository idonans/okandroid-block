package com.okandroid.block.lang;

import com.okandroid.block.core.StorageManager;

import java.util.UUID;

public class AppID {

    private static final String KEY_APP_ID = "_app_id";

    private AppID() {
    }

    public static String get(StorageManager storageManager) {
        return storageManager.getOrSetLock(StorageManager.NAMESPACE_SETTING, KEY_APP_ID, UUID.randomUUID().toString());
    }

}
