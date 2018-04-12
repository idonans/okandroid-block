package com.okandroid.block.util;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class FileUtilTest {
    @Test
    public void getFileExtensionFromUrl() throws Exception {
    }

    @Test
    public void getFilenameFromUrl() throws Exception {
        assertEquals("a.jpg", FileUtil.getFilenameFromUrl("http://abc/a.jpg"));
    }

}