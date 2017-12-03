package com.okandroid.block.lang;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.okandroid.block.AppInit;
import com.okandroid.block.db.SimpleDB;
import com.okandroid.block.thread.Threads;
import java.util.Arrays;

/**
 * 提供 k-v 数据存储, 支持跨进程.
 *
 * <p>提供了 setting 和 cache 两个不同的存储空间，各自独立运行
 */
public class StorageDatabaseProvider extends ContentProvider {

    private final String CLASS_NAME = ClassName.valueOf(this);
    private SimpleDB mDBSetting;
    private SimpleDB mDBCache;

    private static final String AUTHORITY_SUFFIX = ".okandroid.block.storageDatabaseProvider";
    public static final String PATH_SETTING = "setting";
    public static final String PATH_CACHE = "cache";
    private String mAuthority;

    public static Uri getSettingUri(Context context) {
        String packageName = context.getPackageName();
        return Uri.parse("content://" + packageName + AUTHORITY_SUFFIX + "/" + PATH_SETTING);
    }

    public static Uri getCacheUri(Context context) {
        String packageName = context.getPackageName();
        return Uri.parse("content://" + packageName + AUTHORITY_SUFFIX + "/" + PATH_CACHE);
    }

    private static final int URI_SETTING = 1;
    private static final int URI_CACHE = 2;
    private UriMatcher mUriMatcher;

    public static final String COLUMN_KEY = "c_key";
    public static final String COLUMN_VALUE = "c_value";

    @Override
    public boolean onCreate() {
        AppInit.init(getContext());

        Log.v(CLASS_NAME, "init");
        mDBSetting = new SimpleDB("_setting");
        mDBCache = new SimpleDB("_cache");
        Threads.postBackgroundAfterLooper(
                new Runnable() {
                    @Override
                    public void run() {
                        mDBSetting.trim(5000);
                        mDBCache.trim(5000);
                    }
                });

        String packageName = getContext().getPackageName();
        mAuthority = packageName + AUTHORITY_SUFFIX;

        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(mAuthority, PATH_SETTING, URI_SETTING);
        mUriMatcher.addURI(mAuthority, PATH_CACHE, URI_CACHE);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder) {
        final SimpleDB db = getTargetDatabase(uri);
        if (db == null) {
            return null;
        }

        if (selectionArgs == null || selectionArgs.length != 1) {
            new IllegalArgumentException(
                            "error selection args " + Arrays.deepToString(selectionArgs))
                    .printStackTrace();
            return null;
        }

        final String key = selectionArgs[0];

        String[] columnNames = {COLUMN_VALUE};
        Object[] values = {db.get(key)};
        final MatrixCursor cursor = new MatrixCursor(columnNames, 1);
        cursor.addRow(values);

        Threads.postBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        db.touch(key);
                    }
                });

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SimpleDB db = getTargetDatabase(uri);
        if (db == null) {
            return null;
        }

        if (values == null) {
            return null;
        }

        String key = values.getAsString(COLUMN_KEY);
        String value = values.getAsString(COLUMN_VALUE);

        db.set(key, value);

        getContext().getContentResolver().notifyChange(uri, null);

        return null;
    }

    @Override
    public int delete(
            @NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SimpleDB db = getTargetDatabase(uri);
        if (db == null) {
            return 0;
        }

        if (selectionArgs == null || selectionArgs.length != 1) {
            new IllegalArgumentException(
                            "error selection args " + Arrays.deepToString(selectionArgs))
                    .printStackTrace();
            return 0;
        }

        db.remove(selectionArgs[0]);

        getContext().getContentResolver().notifyChange(uri, null);

        return 1;
    }

    @Override
    public int update(
            @NonNull Uri uri,
            @Nullable ContentValues values,
            @Nullable String selection,
            @Nullable String[] selectionArgs) {
        SimpleDB db = getTargetDatabase(uri);
        if (db == null) {
            return 0;
        }

        db.printAllRows();

        return 0;
    }

    @Nullable
    private SimpleDB getTargetDatabase(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case URI_CACHE:
                return mDBCache;
            case URI_SETTING:
                return mDBSetting;
            default:
                new IllegalArgumentException("target database not found for uri " + uri)
                        .printStackTrace();
                return null;
        }
    }
}
